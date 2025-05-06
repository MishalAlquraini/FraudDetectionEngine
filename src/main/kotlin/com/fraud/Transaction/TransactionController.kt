package com.fraud.transaction

import com.fraud.Transaction.TransactionEntity
import com.fraud.Transaction.TransactionService
import com.fraud.Transaction.TransferDto
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
    fun getTransactions(@RequestParam accountId: Long?): ResponseEntity<Any> {
        if (accountId == null || accountId <= 0) {
            return ResponseEntity.badRequest().body("Invalid accountId. It must be a positive number.")
        }
        val transactions = transactionService.getTransactionsByAccountId(accountId)
        return ResponseEntity.ok(transactions)
    }
    }

