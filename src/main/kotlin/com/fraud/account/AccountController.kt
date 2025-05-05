package com.fraud.account

import com.fraud.User.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AccountController (
    private val accountsService: AccountService,
    private val userRepository: UserRepository


) {

    @PostMapping("/accounts/v1/accounts")
    fun createAccount(principal: Principal) : ResponseEntity<*>{

        print(principal)
        val email = principal.name
        val user = userRepository.findByEmail(email)
            ?: return ResponseEntity.badRequest().body("User not found")

        val userId = user.id ?: return ResponseEntity.badRequest().body("User ID is missing")
        accountsService.createAccount(userId)

        return ResponseEntity.ok("Account created successfully")

        }
}

