package com.abrsoftware.chirp.domain.exception

class UserAlreadyExistsException: RuntimeException(
    "User already exists with this username"
) {
}