package com.fraud.transaction

import com.fraud.User.UserRepository
import com.fraud.account.unauthorizedException
import com.fraud.transaction.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
@RequestMapping("/transaction")
class TransactionController(
    private val transactionService: TransactionService,
    private val userRepository: UserRepository

) {

    @PostMapping
    fun transfer(@RequestBody request: TransferDto): ResponseEntity<String> {
        return transactionService.transfer(request)
        }

    @GetMapping("/transactions/{accountNumber}")
    fun getTransactions(@PathVariable accountNumber: String?): ResponseEntity<Any> {
        if (accountNumber.isNullOrBlank()) {
            return ResponseEntity.badRequest().body("Invalid account number. It must be a non-empty string.")
        }
        return transactionService.getTransactionsByAccountNumber(accountNumber)
    }


    @PostMapping("/deposit/{accountNumber}")
    fun deposit(principal: Principal,
        @PathVariable accountNumber: String,
        @RequestBody request: DepositWithdrawDto
    ): ResponseEntity<String> {
        val email = principal.name
        val user = userRepository.findByEmail(email) ?: throw unauthorizedException()
        return transactionService.deposit(user, accountNumber, request)
    }

    @PostMapping("/withdraw/{accountNumber}")
    fun withdraw(
        @PathVariable accountNumber: String,
        @RequestBody request: WithdrawDto
    ): ResponseEntity<String> {
        return transactionService.withdraw(accountNumber, request)
    }

    }

