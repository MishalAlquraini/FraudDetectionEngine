package com.fraud.Account


import com.fraud.User.UserRepository
import jakarta.inject.Named
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*


@Named
class AccountService(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) {


    fun createAccount(userId: Long): Account {
        val user = userRepository.findById(userId).get()

        val newAccount = AccountEntity(
            user=user,
            balance = BigDecimal.ZERO,
            isFrozen = false,
            isActive = true,
            accountNumber = UUID.randomUUID().toString(),
            createdAt = LocalDateTime.now())


        return accountRepository.save(newAccount).let {
            Account(
                userId = it.user?.id,
                accountNumber = it.accountNumber,
                balance = it.balance,
                isFrozen = it.isFrozen,
                isActive = it.isActive,
                createdAt = it.createdAt
            )
        }

    }
}



data class Account(
    val userId: Long?,
    val accountNumber: String,
    val balance: BigDecimal,
    val isFrozen: Boolean,
    val isActive: Boolean,
    val createdAt: LocalDateTime
)