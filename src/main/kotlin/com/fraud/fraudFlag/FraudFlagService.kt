package com.fraud.fraudFlag

import com.fraud.account.AccountRepository
import com.fraud.transaction.TransactionEntity
import com.fraud.transaction.TransactionRepository
import com.fraud.transaction.TransactionStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class FraudFlagService(
    private val fraudFlagRepository: FraudFlagRepository,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {

    fun reviewFlag(id:Long):ReviewFlagResponse{
        val flag = fraudFlagRepository.findById(id).orElseThrow {
            IllegalArgumentException("Fraud flag with ID $id not found")
        }

        val updatedFlag = flag.copy(reviewedByAdmin = true)
        fraudFlagRepository.save(updatedFlag)

        return ReviewFlagResponse(
            transactionId = updatedFlag.transaction.id ?: 0L,
            reviewed = true,
            severity = updatedFlag.severity,
            message = "Flag reviewed successfully"
        )
    }

    fun evaluateTransaction(transaction: TransactionEntity) : Boolean {
        val reasons = mutableListOf<String>()
        var severity = SeverityLevel.LOW
        var status = TransactionStatus.SUCCESS
        var freeze = false

        // High amount check
        if (transaction.amount > BigDecimal("10000")) {
            reasons.add("Amount exceeds threshold")
            severity = SeverityLevel.HIGH
            freeze = true
        }

        val senderAccountNumber = transaction.senderAccount?.accountNumber

        // Location mismatch check
        val lastTwoTransactions = if (senderAccountNumber != null) {
            transactionRepository.findTop2BySenderAccountAccountNumberOrderByTimestamp(senderAccountNumber)
        } else {
            emptyList()
        }

        if (lastTwoTransactions.size == 2) {
            val previousTransaction = lastTwoTransactions[1]
            if (
                transaction.location != null &&
                previousTransaction.location != null &&
                transaction.location != previousTransaction.location
            ) {
                reasons.add("Location mismatch with last transaction")
                severity = SeverityLevel.MEDIUM
                status = TransactionStatus.PENDING
            }
        }

        // Rapid transactions check
        val oneMinuteAgo = transaction.timestamp.minusMinutes(1)
        val rapidTransactionCount = if (senderAccountNumber != null) {
            transactionRepository.countBySenderAccountAccountNumberAndTimestampAfter(senderAccountNumber, oneMinuteAgo)
        } else {
            0
        }

        if (rapidTransactionCount > 3) {
            reasons.add("More than 3 transactions in 1 minute")
            severity = SeverityLevel.HIGH
            freeze = true
        }

        // If anything suspicious, save fraud flag
        if (reasons.isNotEmpty()) {
            val flaggedTransaction = transaction.copy(isFlagged = true, status = TransactionStatus.PENDING)
            if (status == TransactionStatus.SUCCESS && (freeze != false)){
                transactionRepository.save(flaggedTransaction)
            }

            val flag = FraudFlagEntity(
                transaction = transaction,
                reason = reasons.joinToString(", "),
                severity = severity,
            )
            fraudFlagRepository.save(flag)

            if(freeze)
            {
                val acc = accountRepository.findByAccountNumber(senderAccountNumber!!)!!
                acc.isFrozen = true
                accountRepository.save(acc)
            }
        }

        return reasons.isEmpty() && freeze == false

    }
}


data class ReviewFlagResponse(
    val transactionId: Long,
    val reviewed: Boolean,
    val severity: SeverityLevel,
    val message: String
)