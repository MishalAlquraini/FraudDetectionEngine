package com.fraud.authentication.JWT

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.*
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthenticationRequest):  ResponseEntity<*>  {
        val authToken = UsernamePasswordAuthenticationToken(authRequest.email, authRequest.password)
        try {
            authenticationManager.authenticate(authToken)
        } catch (e: BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password")
        } catch (e: DisabledException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is disabled")
        } catch (e: LockedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is locked")
        }

        val token = jwtService.generateToken(authRequest.email)
        return ResponseEntity.ok(AuthenticationResponse(token))
    }
}

data class AuthenticationRequest(
    val email: String,
    val password: String
)

data class AuthenticationResponse(
    val token: String
)