package com.fraud.transaction

import com.fraud.User.UserEntity
import com.fraud.account.AccountEntity
import com.fraud.account.AccountRepository
import com.fraud.account.unauthorizedException
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
        val approved = fraudFlagService.evaluateTransaction(transaction)
        if (approved) {
            sender.balance -= request.amount
            receiver.balance += request.amount

            accountRepository.save(sender)
            accountRepository.save(receiver)
        }

        return ResponseEntity.ok("Transaction Successful")
    }

    fun getTransactionsByAccountNumber(accountNumber: String): ResponseEntity<Any> {
        val transactions = transactionRepository
            .findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(accountNumber, accountNumber).reversed()

        return ResponseEntity.ok(transactions)
    }


    fun deposit(user:UserEntity,accountNumber: String, request: DepositWithdrawDto): ResponseEntity<String> {
        val destinationAccount = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        if (destinationAccount.isFrozen) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is frozen")
        }
        val sourceAccount = accountRepository.findByAccountNumber(request.sourceAccount) ?: AccountEntity()
        if (user.id != sourceAccount.user?.id) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this account")
        }
        if (sourceAccount.balance >= request.amount) {
            val transaction = TransactionEntity(
                senderAccount = sourceAccount,
                receiverAccount = destinationAccount,
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
            val approved = fraudFlagService.evaluateTransaction(transaction)
            if (approved) {
                destinationAccount.balance += request.amount
                sourceAccount.balance-=request.amount
                accountRepository.save(destinationAccount)
                accountRepository.save(sourceAccount)
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Funds")
        }



        return ResponseEntity.ok("Deposit successful")
    }

    fun withdraw(accountNumber: String, request: WithdrawDto): ResponseEntity<String> {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        if (account.isFrozen) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is frozen")
        }

        if (account.balance < request.amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance")
        }

//        account.balance -= request.amount
//        accountRepository.save(account)

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
        val approved = fraudFlagService.evaluateTransaction(transaction)
        if (approved) {
            account.balance -= request.amount
            accountRepository.save(account)
        }

        return ResponseEntity.ok("Withdrawal successful")
    }

}