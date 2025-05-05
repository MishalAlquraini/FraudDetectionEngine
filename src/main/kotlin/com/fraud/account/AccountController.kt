package com.fraud.account

import com.fraud.User.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.security.Principal

@RestController
class AccountController (
    private val accountsService: AccountService,
    private val userRepository: UserRepository


) {

    @PostMapping("/accounts/v1/accounts")
    fun createAccount(@RequestBody request: CreateAccountRequest) {
        accountsService.createAccount(request.userId)
        ResponseEntity.ok("Account created successfully")
//        val name = principal.name
//        val user = userRepository.findByName(name)
//            ?: return ResponseEntity.badRequest().body("User not found")
//
//        val userId = user.id ?: return ResponseEntity.badRequest().body("User ID is missing")
//        accountsService.createAccount(userId)
//
//        return ResponseEntity.ok("Account created successfully")
//
        }
}

data class CreateAccountRequest(
    val userId: Long,

)