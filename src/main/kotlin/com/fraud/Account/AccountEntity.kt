package com.fraud.Account

import com.fraud.User.UserEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name="accounts")
data class AccountEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val userId: UserEntity? = null,

    val accountNumber: String,
    val balance: BigDecimal,
    val isFrozen: Boolean,
    val isActive: Boolean,
    val createdAt: LocalDateTime
){
    constructor() : this(null, null, "", BigDecimal.ZERO,
        false, false, LocalDateTime.now())
}