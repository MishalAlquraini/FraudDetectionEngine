package com.fraud.transaction

import com.fraud.account.AccountRepository
import com.fraud.fraudFlag.FraudFlagService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.plus




@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val fraudFlagService: FraudFlagService
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
        val apoorved = fraudFlagService.evaluateTransaction(transaction)
        if (apoorved) {
            sender.balance -= request.amount
            receiver.balance += request.amount

            accountRepository.save(sender)
            accountRepository.save(receiver)
        }

        if (apoorved) {
            return ResponseEntity.ok("Transaction Successful")
        }
        return ResponseEntity.ok("Transaction is Marked for Admin Review")
    }

    fun getTransactionsByAccountNumber(accountNumber: String): ResponseEntity<Any> {
        val transactions = transactionRepository
            .findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(accountNumber, accountNumber)

        return ResponseEntity.ok(transactions)
    }


    fun deposit(accountNumber: String, request: DepositWithdrawDto): ResponseEntity<String> {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        if (account.isFrozen) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is frozen")
        }
        val transaction = TransactionEntity(
            senderAccount = null,
            receiverAccount = account,
            amount = request.amount,
            timestamp = LocalDateTime.now(),
            location = request.location,
            status = TransactionStatus.SUCCESS,
            deviceId = request.deviceId,
            ipAddress = request.ipAddress,
            isFlagged = false,
            isDeposit = true,
            isWithdrawal = false
        )

        transactionRepository.save(transaction)
        val apoorved = fraudFlagService.evaluateTransaction(transaction)
        if (apoorved) {
            account.balance += request.amount
            accountRepository.save(account)
        }
        return ResponseEntity.ok("Deposit successful")
    }

    fun withdraw(accountNumber: String, request: DepositWithdrawDto): ResponseEntity<String> {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        if (account.isFrozen) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is frozen")
        }

        if (account.balance < request.amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance")
        }

        account.balance -= request.amount
        accountRepository.save(account)

        val transaction = TransactionEntity(
            senderAccount = account,
            receiverAccount = null,
            amount = request.amount,
            timestamp = LocalDateTime.now(),
            location = request.location,
            status = TransactionStatus.SUCCESS,
            deviceId = request.deviceId,
            ipAddress = request.ipAddress,
            isFlagged = false,
            isDeposit = false,
            isWithdrawal = true
        )

        transactionRepository.save(transaction)
        val apoorved = fraudFlagService.evaluateTransaction(transaction)
        if (apoorved) {
            account.balance -= request.amount
            accountRepository.save(account)
        }

        return ResponseEntity.ok("Withdrawal successful")
    }

}