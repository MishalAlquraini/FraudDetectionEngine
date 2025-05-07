package com.fraud.transaction

import com.fraud.transaction.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/transaction")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping
    fun transfer(@RequestBody request: TransferDto): ResponseEntity<String> {
        return transactionService.transfer(request)
        }

    @GetMapping("/transactions")
    fun getTransactions(@RequestParam accountId: String?): ResponseEntity<Any> {
        if (accountId == null) {
            return ResponseEntity.badRequest().body("Invalid accountId. It must be a positive number.")
        }
        val transactions = transactionService.getTransactionsByAccountId(accountId)
        return ResponseEntity.ok(transactions)
    }
    }

