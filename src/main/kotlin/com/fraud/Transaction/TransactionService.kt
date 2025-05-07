package com.fraud.Transaction

import com.fraud.account.AccountRepository
import com.fraud.transaction.TransactionEntity
import com.fraud.transaction.TransactionStatus
import com.fraud.transaction.TransferDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    fun transfer(request: TransferDto): ResponseEntity<String> {
        val sender = accountRepository.findByAccountNumber(request.senderAccountId)
            ?: throw IllegalArgumentException("Source account not found")

        val receiver = accountRepository.findByAccountNumber(request.receiverAccountId)
            ?: throw IllegalArgumentException("Destination account not found")

        if (sender.isFrozen || receiver.isFrozen) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("One of the accounts is frozen")
        }
        if (sender.balance < request.amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance")
        }

        sender.balance -= request.amount
        receiver.balance += request.amount

        accountRepository.save(sender)
        accountRepository.save(receiver)

        val transaction = TransactionEntity(
            senderAccount = sender,
            receiverAccount = receiver,
            amount = request.amount,
            timestamp = LocalDateTime.now(),
            location = request.location,
            status = TransactionStatus.SUCCESS,
            deviceId = request.deviceId,
            ipAddress = request.ipAddress,
            isFlagged = false
        )
        transactionRepository.save(transaction)

        return ResponseEntity.ok("Transaction Successful")
    }

    fun getTransactionsByAccountId(accountId: String): ResponseEntity<List<TransactionEntity>> {
        val transactions = transactionRepository.findBySenderAccountAccountNumberOrReceiverAccountAccountNumber (
            accountId, accountId)
        return ResponseEntity.ok(transactions)
    }

}