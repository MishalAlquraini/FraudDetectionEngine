package com.fraud.Admin


import com.fraud.account.AccountRepository
import jakarta.inject.Named
import java.math.BigDecimal


@Named
class AdminService (
    private val accountRepository: AccountRepository,

    ) {

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