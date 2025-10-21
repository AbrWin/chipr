package com.abrsoftware.chirp.api.mappers

import com.abrsoftware.chirp.api.dto.AuthenticatedUserDto
import com.abrsoftware.chirp.api.dto.UserDto
import com.abrsoftware.chirp.domain.model.AuthenticatedUser
import com.abrsoftware.chirp.domain.model.User


fun AuthenticatedUser.toAuthenticatedUserDto(): AuthenticatedUserDto {
    return AuthenticatedUserDto(
        user = user.toUserDto(),
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasEmailVerified
    )
}