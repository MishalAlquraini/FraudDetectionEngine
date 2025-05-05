package com.fraud.account

import com.fraud.User.UserEntity
import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface AccountRepository : JpaRepository<AccountEntity, Long>{
    fun findByUser(user: UserEntity): AccountEntity?
    fun findByAccountNumber(accountNumber: String): AccountEntity?

}