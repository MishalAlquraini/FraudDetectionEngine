package com.fraud.Admin


import com.fraud.transaction.TransactionRepository
import com.fraud.transaction.TransactionEntity
import com.fraud.account.AccountRepository
import jakarta.inject.Named
import java.math.BigDecimal
import com.fraud.transaction.TransferDto


@Named
class AdminService (
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    ) {

    fun listFlaggedTransactions(
        request: TransactionFilter
    ): List<TransactionEntity?> {
        val transaction = transactionRepository.findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(
            request.accountId,
            request.accountId
        ).filter { it.isFlagged == true }


        return transaction

    }

    fun getAccounts(): List<AccountData> {
        return accountRepository.findAll().map {
            AccountData(
                userId = it.user?.id,
                accountNumber = it.accountNumber,
                isActive = it.isActive,
                isFrozen = it.isFrozen,
                balance = it.balance
            )
        }

    }
}

data class AccountData(
        val userId: Long?,
        val accountNumber: String,
        val balance: BigDecimal,
        val isFrozen: Boolean,
        val isActive: Boolean,
    )

data class TransactionFilter(
    val accountId: String,
)