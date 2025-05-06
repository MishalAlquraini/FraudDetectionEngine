package com.fraud.Transaction


import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findBySenderAccountIdOrReceiverAccountId(senderId: Long, receiverId: Long): List<TransactionEntity>
}


