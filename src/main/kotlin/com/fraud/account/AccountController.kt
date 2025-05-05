package com.fraud.account

import com.fraud.User.UserEntity
import com.fraud.User.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AccountController (
    private val accountsService: AccountService,
    private val userRepository: UserRepository


) {

    @GetMapping("/accounts/v1/accounts")
    fun getAccounts(principal: Principal){

        TODO()
//        val email = principal.name
//        val user = userRepository.findByEmail(email) ?: UserEntity()
//         accountsService.checkBalance(user)

    }

    @GetMapping("/accounts/{accountNumber}/balance")
    fun checkBalance(principal: Principal, @PathVariable accountNumber:String):AccountBalance{
        val email = principal.name
        val user = userRepository.findByEmail(email)
            ?: UserEntity()

         return accountsService.checkBalance(user,accountNumber)

    }

    @PostMapping("/accounts/v1/accounts")
    fun createAccount(principal: Principal) : ResponseEntity<*>{

        val email = principal.name
        val user = userRepository.findByEmail(email)
            ?: return ResponseEntity.badRequest().body("User not found")

        val userId = user.id ?: return ResponseEntity.badRequest().body("User ID is missing")
        accountsService.createAccount(userId)

        return ResponseEntity.ok("Account created successfully")

        }
}

