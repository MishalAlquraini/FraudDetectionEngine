package com.fraud.account


import com.fraud.User.UserEntity
import com.fraud.User.UserRepository
import jakarta.inject.Named
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ResponseStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*


@Named
class AccountService(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) {

    object AccountNumberGenerator {
        private var serial = 1

        fun generate(): String {
            val bankCode = "12045"
            val randomPart = (100..999).random().toString()
            val serialPart = serial.toString().padStart(1, '0')
            serial += 1
            return "$bankCode$randomPart$serialPart"
        }
    }
    fun listAccounts(user: UserEntity): List<AccountBalance>{
        val account = accountRepository.findAllByUser(user) ?: emptyList()
        return account.map { AccountBalance(
            accountNumber = it.accountNumber,
            balance = it.balance
        ) }

    }
fun checkBalance(user: UserEntity, accountNumber: String): AccountBalance{
    val account = accountRepository.findByAccountNumber(accountNumber) ?: AccountEntity()
    if (user.id != account.user?.id) {
        throw unauthorizedException()
    }
        return account.let {
            AccountBalance(
                accountNumber = it.accountNumber,
                balance = it.balance
            )
        }


}


    fun createAccount(userId: Long): Account {
        val user = userRepository.findById(userId).get()

        val newAccount = AccountEntity(
            user=user,
            balance = BigDecimal.ZERO,
            isFrozen = false,
            isActive = true,
//            accountNumber = (100_000_000..999_999_999).random().toString(),
            accountNumber = AccountNumberGenerator.generate(),
            createdAt = LocalDateTime.now())


        return accountRepository.save(newAccount).let {
            Account(
                userId = it.user?.id,
                accountNumber = it.accountNumber,
                balance = it.balance,
//                isFrozen = it.isFrozen,
//                isActive = it.isActive,
                createdAt = it.createdAt
            )
        }

    }


    fun deactivateAccount(accountNumber: String):ResponseEntity<*>{
        val foundAccount = accountRepository.findByAccountNumber(accountNumber)?: throw IllegalArgumentException("Account not found")
        foundAccount.isActive = !foundAccount.isActive
        accountRepository.save(foundAccount)

        return if (foundAccount.isActive)
            ResponseEntity.ok("Account activated successfully")
        else
            ResponseEntity.ok("Account deactivated successfully")    }
}



data class Account(
    val userId: Long?,
    val accountNumber: String,
    val balance: BigDecimal,
//    val isFrozen: Boolean,
//    val isActive: Boolean,
    val createdAt: LocalDateTime
)

data class AccountBalance(
    val accountNumber: String,
    val balance: BigDecimal
)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class unauthorizedException(message: String = "You are not authorized to access this account") : RuntimeException(message)