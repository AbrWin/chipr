package com.abrsoftware.chirp.service

import com.abrsoftware.chirp.domain.exception.InvalidCredentialsException
import com.abrsoftware.chirp.domain.exception.InvalidTokenException
import com.abrsoftware.chirp.domain.exception.SamePasswordException
import com.abrsoftware.chirp.domain.exception.UserNotFoundException
import com.abrsoftware.chirp.domain.model.EmailVerificationToken
import com.abrsoftware.chirp.domain.model.UserId
import com.abrsoftware.chirp.infra.database.entities.PasswordResetTokenEntity
import com.abrsoftware.chirp.infra.database.repositories.PasswordTokenRepository
import com.abrsoftware.chirp.infra.database.repositories.RefreshTokenRepository
import com.abrsoftware.chirp.infra.database.repositories.UserRepository
import com.abrsoftware.chirp.infra.security.PasswordEncoder
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.Instant.now
import java.time.temporal.ChronoUnit

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val passwordRsetTokenRepository: PasswordTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    @param:Value("\${chirp.email.reset.reset.password.expiry.minutes")
    private val expiryMinutes: Long,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @Transactional
    fun requestResetPassword(email: String) {
        val user = userRepository.findByEmail(email) ?: return

        passwordRsetTokenRepository.invalidateActiveTokensForUser(user)
        val token = PasswordResetTokenEntity(
            user = user,
            expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES),
        )

        passwordRsetTokenRepository.save(token)

        //TODO: Inform notification service about password reset triggert to send mail
    }

    @Transactional
    fun resetPassword(token: String, newPassword: String) {
        val resetToken = passwordRsetTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Invalid password reset token")

        if (resetToken.isUsed) {
            throw InvalidTokenException("Email verification token is already used.")
        }

        if (resetToken.isExpired) {
            throw InvalidTokenException("Email verification token has already expired.")
        }
        val user = resetToken.user

        if (passwordEncoder.matches(newPassword, user.hashedPassword)) {
            throw SamePasswordException()
        }

        val hashedNewPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            user.apply {
                this.hashedPassword = hashedNewPassword
            }
        )

        passwordRsetTokenRepository.save(
            resetToken.apply {
                this.usedAt = Instant.now()
            }
        )

        refreshTokenRepository.deleteByUserId(user.id!!)
    }

    @Transactional
    fun changePassword(
        userId: UserId,
        oldPassword: String,
        newPassword: String
    ) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        if(passwordEncoder.matches(oldPassword, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        if(oldPassword == newPassword) {
            throw SamePasswordException()
        }

        refreshTokenRepository.deleteByUserId(user.id!!)
        val newHashedPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            user.apply {
                this.hashedPassword = newHashedPassword
            }
        )
    }

    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens() {
        passwordRsetTokenRepository.deleteByExpiresAtLessThan(
            now = now()
        )
    }


}