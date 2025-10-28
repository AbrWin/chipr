package com.abrsoftware.chirp.infra.database.mappers

import com.abrsoftware.chirp.domain.model.EmailVerificationToken
import com.abrsoftware.chirp.infra.database.entities.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}