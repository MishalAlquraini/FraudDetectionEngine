package com.fraud.Admin


import com.fraud.Transaction.TransactionRepository
import com.fraud.transaction.TransactionEntity
import com.fraud.account.AccountRepository
import com.fraud.fraudFlag.FinalStatus
import jakarta.inject.Named
import java.math.BigDecimal
import com.fraud.transaction.TransferDto


@Named
class AdminService (
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
//    private val fraudFlagRepository:FraudFlagRepository
    ) {

    fun listAllTransactions(request: TransactionFilter
    ): List<TransactionEntity?> {
        val transaction = transactionRepository.findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(
            request.accountId,
            request.accountId
        ).filter { it.isFlagged }


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

//    fun reviewFlag(transactionId:String){
//        val foundFlag = fraudFlagRepository.findByTransactionId(transactionId)
//        foundFlag.reviewedByAdmin = true
//        foundFlag.finalStatus = FinalStatus.TRUE
//        fraudFlagRepository.save(foundFlag)
//    }
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