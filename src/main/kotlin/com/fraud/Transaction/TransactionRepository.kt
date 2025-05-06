package com.fraud.Transaction


import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(senderAccountNumber: String, receiverAccountNumber: String): List<TransactionEntity>
}


