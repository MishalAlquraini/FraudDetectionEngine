package com.fraud.authentication.JWT

import com.fraud.User.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey


@Component
class JwtService(
    private val userRepository: UserRepository
) {

    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val expirationMs: Long = 1000 * 60 * 60

    fun generateToken(email : String): String {
        val now = Date()
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("User not found ...")
        val expiry = Date(now.time + expirationMs)

        return Jwts.builder()
            //.claim("roles",user.role)
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey)
            .compact()
    }

    fun extractEmail(token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject

    fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun isTokenValid(token: String, email: String): Boolean {
        return try {
            extractEmail(token) == email
        } catch (e: Exception) {
            false
        }
    }
}