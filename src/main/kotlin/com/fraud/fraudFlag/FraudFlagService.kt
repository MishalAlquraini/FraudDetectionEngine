package com.fraud.fraudFlag

import com.fraud.transaction.TransactionEntity
import com.fraud.transaction.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class FraudFlagService(
    private val fraudFlagRepository: FraudFlagRepository,
    private val transactionRepository: TransactionRepository
) {

    fun evaluateTransaction(transaction: TransactionEntity) {
        val reasons = mutableListOf<String>()
        var severity = SeverityLevel.LOW

        // High amount check
        if (transaction.amount > BigDecimal("10000")) {
            reasons.add("Amount exceeds threshold")
            severity = SeverityLevel.HIGH
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
        }

        // If anything suspicious, save fraud flag
        if (reasons.isNotEmpty()) {
            val flaggedTransaction = transaction.copy(isFlagged = true)
            transactionRepository.save(flaggedTransaction)

            val flag = FraudFlagEntity(
                transaction = transaction,
                reason = reasons.joinToString(", "),
                severity = severity
            )
            fraudFlagRepository.save(flag)
        }
    }
}
