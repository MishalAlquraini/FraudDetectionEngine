package com.fraud.Admin

import com.fraud.Transaction.TransactionEntity
import com.fraud.Transaction.TransactionRepository
import com.fraud.Transaction.TransferDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminService(
    private val transactionRepository: TransactionRepository,
){
    fun listAllTransactions(request: TransferDto
        ): List<TransactionEntity?> {
        val transaction = transactionRepository.findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(
            request.senderAccountId,
            request.receiverAccountId)
        return transaction

    }


}