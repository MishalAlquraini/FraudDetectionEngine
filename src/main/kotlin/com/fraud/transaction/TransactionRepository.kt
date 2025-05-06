package com.fraud.transaction


import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(senderId: String, receiverId: String): List<TransactionEntity>
}


