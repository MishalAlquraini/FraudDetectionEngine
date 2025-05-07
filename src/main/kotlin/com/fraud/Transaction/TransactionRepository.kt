package com.fraud.Transaction


import com.fraud.transaction.TransactionEntity
import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(senderAccountNumber: String, receiverAccountNumber: String): List<TransactionEntity>
}


