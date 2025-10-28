package com.abrsoftware.chirp.infra.database.repositories

import com.abrsoftware.chirp.domain.model.User
import com.abrsoftware.chirp.infra.database.entities.EmailVerificationTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface EmailVerificationTokenRepository: JpaRepository<EmailVerificationTokenEntity, Long> {
    fun findByToken(token: String): EmailVerificationTokenEntity?
    fun deleteByExpiresAtLessThan(now: Instant)
    fun findByUserAndUsedAtIsNull(user: User): List<EmailVerificationTokenEntity?>
}