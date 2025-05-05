package com.fraud.transaction

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/transaction")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping
    fun transfer(@RequestBody request: TransferDto): ResponseEntity<String> {
        return transactionService.transfer(request)
        }
    }

