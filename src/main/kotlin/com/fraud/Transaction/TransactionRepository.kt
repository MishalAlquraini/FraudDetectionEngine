package com.fraud.transaction


import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findTop2BySenderAccountAccountNumberOrderByTimestamp(accountNumber: String): List<TransactionEntity>
    fun countBySenderAccountAccountNumberAndTimestampAfter(accountNumber: String, timestamp: LocalDateTime): Int

    fun findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(senderAccountNumber: String, receiverAccountNumber: String): List<TransactionEntity>
}


