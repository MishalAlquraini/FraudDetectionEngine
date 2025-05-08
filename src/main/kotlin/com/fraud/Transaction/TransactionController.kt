package com.fraud.transaction

import com.fraud.transaction.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/transaction")
class TransactionController(
    private val transactionService: TransactionService,
) {

    @PostMapping
    fun transfer(@RequestBody request: TransferDto): ResponseEntity<String> {
        return transactionService.transfer(request)
        }

    @GetMapping("/transactions")
    fun getTransactions(@RequestParam accountNumber: String?): ResponseEntity<Any> {
        if (accountNumber.isNullOrBlank()) {
            return ResponseEntity.badRequest().body("Invalid account number. It must be a non-empty string.")
        }
        return transactionService.getTransactionsByAccountNumber(accountNumber)
    }


    @PostMapping("/deposit/{accountNumber}")
    fun deposit(
        @PathVariable accountNumber: String,
        @RequestBody request: DepositWithdrawDto
    ): ResponseEntity<String> {
        return transactionService.deposit(accountNumber, request)
    }

    @PostMapping("/withdraw/{accountNumber}")
    fun withdraw(
        @PathVariable accountNumber: String,
        @RequestBody request: DepositWithdrawDto
    ): ResponseEntity<String> {
        return transactionService.withdraw(accountNumber, request)
    }

    }

