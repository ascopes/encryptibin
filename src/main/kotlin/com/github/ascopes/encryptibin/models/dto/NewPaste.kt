package com.github.ascopes.encryptibin.models.dto

import com.github.ascopes.encryptibin.constraints.IsBase64
import com.github.ascopes.encryptibin.constraints.MaxDuration
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import org.springframework.validation.annotation.Validated

@Validated
data class NewPaste(
    @get:Min(1)
    @get:MaxDuration
    val expireAfter: Long,

    @get:NotBlank
    @get:IsBase64
    @get:Size(max = 100_000_000)
    val content: String
)
