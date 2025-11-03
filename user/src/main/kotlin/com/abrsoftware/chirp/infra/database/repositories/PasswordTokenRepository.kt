package com.abrsoftware.chirp.infra.database.repositories

import com.abrsoftware.chirp.infra.database.entities.EmailVerificationTokenEntity
import com.abrsoftware.chirp.infra.database.entities.PasswordResetTokenEntity
import com.abrsoftware.chirp.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface PasswordTokenRepository: JpaRepository<PasswordResetTokenEntity, Long> {
    fun findByToken(token: String): PasswordResetTokenEntity?
    fun deleteByExpiresAtLessThan(now: Instant)

    @Modifying
    @Query("""
        UPDATE PasswordResetTokenEntity p
        SET p.usedAt = CURRENT_TIMESTAMP
        WHERE p.user = :user
    """)
    fun invalidateActiveTokensForUser(user: UserEntity)

}