package com.github.ascopes.encryptibin.service

import com.github.ascopes.encryptibin.models.dao.PasteEntity
import com.github.ascopes.encryptibin.models.dto.ExistingPaste
import com.github.ascopes.encryptibin.models.dto.NewPaste
import com.github.ascopes.encryptibin.models.props.EncryptionProperties
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.Base64
import java.util.UUID
import javax.crypto.Cipher
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class PasteService(
    private val redisTemplate: ReactiveRedisTemplate<String, PasteEntity>,
    private val encryptionProperties: EncryptionProperties
) {
    fun createNewPaste(newPaste: NewPaste): Mono<ExistingPaste> {
        val keyPair = newKeyPair()
        val encrypted = encrypt(newPaste.content, keyPair.private)

        val id = UUID.randomUUID().toString()
        val createdAt = Instant.now(Clock.systemUTC())
        val entity = PasteEntity(createdAt, encrypted)

        val existingPaste = ExistingPaste(createdAt, serializeKey(keyPair.public), newPaste.content)

        return redisTemplate
            .opsForSet()
            .add(id, entity)
            .then(redisTemplate.expire(id, Duration.ofSeconds(newPaste.expireAfter)))
            .thenReturn(existingPaste)
    }

    fun getExistingPaste(id: String, b64PublicKey: String): Mono<ExistingPaste> {
        return redisTemplate
            .opsForValue()
            .get(id)
            .map {
                ExistingPaste(
                    it.createdAt,
                    b64PublicKey,
                    decrypt(it.content, deserializeKey(b64PublicKey))
                )
            }
    }

    private fun newKeyPair() = KeyPairGenerator
        .getInstance(encryptionProperties.algorithm)
        .apply { initialize(encryptionProperties.keySize) }
        .genKeyPair()!!

    private fun encrypt(decryptedBase64: String, privateKey: PrivateKey) = Cipher
        .getInstance(encryptionProperties.algorithm)
        .apply { init(Cipher.ENCRYPT_MODE, privateKey) }
        .doFinal(Base64.getDecoder().decode(decryptedBase64))
        .let { Base64.getEncoder().encodeToString(it) }

    private fun decrypt(encryptedBase64: String, publicKey: PublicKey) = Cipher
        .getInstance(encryptionProperties.algorithm)
        .apply { init(Cipher.DECRYPT_MODE, publicKey) }
        .doFinal(Base64.getDecoder().decode(encryptedBase64))
        .let { Base64.getEncoder().encodeToString(it) }

    private fun serializeKey(publicKey: PublicKey) = KeyFactory
        .getInstance(encryptionProperties.algorithm)
        .getKeySpec(publicKey, X509EncodedKeySpec::class.java)
        .encoded
        .let { Base64.getUrlEncoder().encodeToString(it) }

    private fun deserializeKey(publicKey: String) = Base64
        .getUrlDecoder()
        .decode(publicKey)
        .let { X509EncodedKeySpec(it) }
        .let { KeyFactory.getInstance(encryptionProperties.algorithm).generatePublic(it) }
}