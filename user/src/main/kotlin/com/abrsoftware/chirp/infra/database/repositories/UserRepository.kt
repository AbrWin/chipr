package com.abrsoftware.chirp.infra.database.repositories

import com.abrsoftware.chirp.domain.model.UserId
import com.abrsoftware.chirp.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserId> {
    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): UserEntity?
}