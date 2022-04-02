package com.github.ascopes.encryptibin.models.props

import java.security.NoSuchAlgorithmException
import java.time.Duration
import javax.crypto.Cipher
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties("encryptibin.encryption")
@Validated
class EncryptionProperties : InitializingBean {
    var maxExpireAfter: Duration = Duration.ofDays(31)

    @NotBlank
    var algorithm: String = "RSA"

    @Min(256)
    @Max(4096)
    var keySize: Int = 1024

    override fun afterPropertiesSet() {
        try {
            Cipher.getInstance(algorithm)
        } catch (ex: NoSuchAlgorithmException) {
            throw BeanInitializationException("Algorithm $algorithm does not exist in this JVM", ex)
        }
    }
}