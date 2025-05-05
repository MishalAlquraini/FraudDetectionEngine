package com.fraud.Account

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.math.BigDecimal

class AccountController (
    private val accountsService: AccountService,

    ){

    @PostMapping("/accounts/v1/accounts")
    fun createAccount(@RequestBody request: CreateAccountRequest){
            accountsService.createAccount(request.userId)
            ResponseEntity.ok("Account created successfully")
    }
}


data class CreateAccountRequest(
    val userId: Long
)