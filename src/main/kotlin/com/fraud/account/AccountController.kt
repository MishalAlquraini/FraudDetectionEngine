package com.fraud.account

import com.fraud.User.UserEntity
import com.fraud.User.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AccountController (
    private val accountsService: AccountService,
    private val userRepository: UserRepository


) {

    // GET list of accounts for user
    @GetMapping("/accounts/v1/accounts")
    fun getAccounts(principal: Principal):List<AccountBalance>{

        val email = principal.name
        val user = userRepository.findByEmail(email) ?: UserEntity()
       return accountsService.listAccounts(user)

    }

    // GET balance of a specific accounts for user
    @GetMapping("/accounts/{accountNumber}/balance")
    fun checkBalance(principal: Principal, @PathVariable accountNumber:String):AccountBalance{
        val email = principal.name
        val user = userRepository.findByEmail(email)
            ?: UserEntity()

         return accountsService.checkBalance(user,accountNumber)

    }


    // Create a new account for user
    @PostMapping("/accounts/v1/accounts")
    fun createAccount(principal: Principal) : ResponseEntity<*>{

        val email = principal.name
        val user = userRepository.findByEmail(email)
            ?: return ResponseEntity.badRequest().body("User not found")

        val userId = user.id ?: return ResponseEntity.badRequest().body("User ID is missing")
        accountsService.createAccount(userId)

        return ResponseEntity.ok("Account created successfully")

        }


    //Deactivate account for user
    @PatchMapping("/accounts/{accountNumber}/activate-toggle")
    fun closeAccount (@PathVariable accountNumber: String): ResponseEntity<*> =  accountsService.deactivateAccount(accountNumber)

}

