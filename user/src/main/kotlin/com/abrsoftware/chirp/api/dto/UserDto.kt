package com.abrsoftware.chirp.api.dto

import com.abrsoftware.chirp.domain.model.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
)