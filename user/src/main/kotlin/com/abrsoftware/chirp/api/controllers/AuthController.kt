package com.abrsoftware.chirp.api.controllers

import com.abrsoftware.chirp.api.dto.AuthenticatedUserDto
import com.abrsoftware.chirp.api.dto.LoginRequest
import com.abrsoftware.chirp.api.dto.RegisterRequest
import com.abrsoftware.chirp.api.dto.UserDto
import com.abrsoftware.chirp.api.mappers.toAuthenticatedUserDto
import com.abrsoftware.chirp.api.mappers.toUserDto
import com.abrsoftware.chirp.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): UserDto {
        return authService.register(
            email = body.email,
            username = body.username,
            password = body.password
        ).toUserDto()
    }

    @PostMapping("/login")
    fun logi(
        @Valid @RequestBody body: LoginRequest
    ): AuthenticatedUserDto{
        return authService.login(
            email = body.email,
            password = body.password
        ).toAuthenticatedUserDto()
    }
}