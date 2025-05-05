package com.fraud.account

import org.springframework.data.jpa.repository.JpaRepository


interface AccountRepository : JpaRepository<AccountEntity, Long> {
    fun findByAccountNumber(accountNumber: String): AccountEntity?
}
