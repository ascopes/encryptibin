package com.github.ascopes.encryptibin.config

import com.github.ascopes.encryptibin.models.dao.PasteEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun reactiveRedisTemplate(
        connFactory: ReactiveRedisConnectionFactory
    ) = RedisSerializationContext
        .newSerializationContext<String, PasteEntity>(StringRedisSerializer())
        .value(Jackson2JsonRedisSerializer(PasteEntity::class.java))
        .build()
        .let { ReactiveRedisTemplate(connFactory, it) }
}