package com.fraud.Transaction

import org.springframework.stereotype.Service



@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
)