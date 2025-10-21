package com.abrsoftware.chirp.infra.database.mappers

import com.abrsoftware.chirp.domain.model.User
import com.abrsoftware.chirp.infra.database.entities.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasEmailVerified = hasVerifiedEmail
    )
}