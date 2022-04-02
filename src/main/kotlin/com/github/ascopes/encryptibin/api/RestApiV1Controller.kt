package com.github.ascopes.encryptibin.api

import com.github.ascopes.encryptibin.constraints.IsBase64
import com.github.ascopes.encryptibin.constraints.IsUuid
import com.github.ascopes.encryptibin.models.dto.NewPaste
import com.github.ascopes.encryptibin.service.PasteService
import javax.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api/v1/paste")
@RestController
@Validated
class RestApiV1Controller(private val pasteService: PasteService) {

    @PostMapping
    fun createNewPaste(
        @Valid @RequestBody newPaste: NewPaste
    ) = pasteService.createNewPaste(newPaste)

    @GetMapping("/{id}/{publicKey}")
    fun getExistingPaste(
        @Valid @RequestParam @IsUuid id: String,
        @Valid @RequestParam @IsBase64 publicKey: String
    ) = pasteService.getExistingPaste(id, publicKey)
}