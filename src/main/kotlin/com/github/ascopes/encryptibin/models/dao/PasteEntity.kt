package com.github.ascopes.encryptibin.models.dao

import java.time.Instant

data class PasteEntity(
    val createdAt: Instant,
    val content: String,
)