package com.fraud.Account

import com.fraud.User.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import java.security.Principal

class AccountController (
    private val accountsService: AccountService,
    private val userRepository: UserRepository


) {

    @PostMapping("/accounts/v1/accounts")
    fun createAccount(principal: Principal): ResponseEntity<String> {
        val name = principal.name
        val user = userRepository.findByName(name)
            ?: return ResponseEntity.badRequest().body("User not found")

        val userId = user.id ?: return ResponseEntity.badRequest().body("User ID is missing")
        accountsService.createAccount(userId)

        return ResponseEntity.ok("Account created successfully")
    }
}


