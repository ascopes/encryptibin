# Encryptibin

A basic reactive Spring Boot app that provides an encrypted pastebin using Redis as a backend.

Private keys are never stored, so the server can never decrypt pastes once they have been
encrypted, and all pastes have an expiry.

This is not yet finished and is a WIP.