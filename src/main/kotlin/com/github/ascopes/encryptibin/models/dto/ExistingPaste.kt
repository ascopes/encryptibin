package com.github.ascopes.encryptibin.models.dto

import java.time.Instant

data class ExistingPaste(
    val createdAt: Instant,
    val publicKey: String,
    val content: String
)