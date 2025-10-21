package com.abrsoftware.chirp.service.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.abrsoftware.chirp.domain.exception.InvalidTokenException
import com.abrsoftware.chirp.domain.model.UserId
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date
import java.util.UUID
import kotlin.io.encoding.Base64

@Service
class JwtService(
    @param:Value("\${jwt.secret}") private val secretBase64: String,
    @param:Value("\${jwt.expiration-minutes}") private val expirationMinutes: Int,
) {

    private val secretKey = Keys.hmacShaKeyFor(
        Base64.decode(secretBase64)
    )
    private val accessTokenValidityMs = expirationMinutes * 60 * 60 * 1000L
    val refreshTokenValidityMs = 30 * 24 * 60 * 60 * 1000L

    fun validateAccessToken(token: String): Boolean {
        val claims = paseAllClaims(token) ?: return false
        val tokeType = claims["type"] as? String ?: return false
        return tokeType == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = paseAllClaims(token) ?: return false
        val tokeType = claims["type"] as? String ?: return false
        return tokeType == "refresh"
    }

    fun getUserIdFromToken(token: String): UserId? {
        val claims = paseAllClaims(token) ?: throw InvalidTokenException(
            "Invalid JWT token"
        )
        return UUID.fromString(claims["id"] as? String)
    }

    private fun generateToken(
        userId: UserId,
        type: String,
        expiry: Long
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId.toString())
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: UserId): String {
        return generateToken(
            userId = userId,
            type = "access",
            expiry = accessTokenValidityMs
        )
    }

    fun generateRefresToken(userId: UserId): String {
        return generateToken(
            userId = userId,
            type = "refresh",
            expiry = refreshTokenValidityMs
        )
    }

    private fun paseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer")) {
            token.removePrefix("Bearer")
        } else token

        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }

}