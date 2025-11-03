package com.abrsoftware.chirp.api.controllers

import com.abrsoftware.chirp.api.dto.*
import com.abrsoftware.chirp.api.mappers.toAuthenticatedUserDto
import com.abrsoftware.chirp.api.mappers.toUserDto
import com.abrsoftware.chirp.domain.model.UserId
import com.abrsoftware.chirp.service.AuthService
import com.abrsoftware.chirp.service.EmailVerificationService
import com.abrsoftware.chirp.service.PasswordResetService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService
) {

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
    ): AuthenticatedUserDto {
        return authService.login(
            email = body.email,
            password = body.password
        ).toAuthenticatedUserDto()
    }

    @PostMapping("/refresh")
    fun refresh(
        @Valid @RequestBody body: RefreshRequest
    ): AuthenticatedUserDto {
        return authService
            .refresh(body.refreshToken)
            .toAuthenticatedUserDto()
    }

    @PostMapping("/logout")
    fun logout(
        @Valid @RequestBody body: RefreshRequest
    ) {
        return authService
            .logout(body.refreshToken)
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam token: String
    ) {
        emailVerificationService.verifyEmail(token)
    }


    @PostMapping("/forgot-password")
    fun forgotPassword(
        @Valid @RequestBody body: EmailRequest
    ){
        passwordResetService.requestResetPassword(body.email)
    }

    @PostMapping("/reset-password")
    fun resetPassword(
        @Valid @RequestBody body: ResetPasswordRequest
    ){
        passwordResetService.resetPassword(
            token = body.token,
            newPassword = body.newPassword
        )
    }

    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ) {
        // TODO: Extract request user ID and call service
    }
}