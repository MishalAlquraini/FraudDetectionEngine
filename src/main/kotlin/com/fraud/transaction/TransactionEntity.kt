package com.fraud.transaction

import com.fraud.account.AccountEntity
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sender_account_number", referencedColumnName = "accountNumber")
    val senderAccount: AccountEntity? = null,

    @ManyToOne
    @JoinColumn(name = "receiver_account_number", referencedColumnName = "accountNumber")
    val receiverAccount: AccountEntity? = null,

    val amount: BigDecimal= BigDecimal.ZERO,

    val timestamp: LocalDateTime = LocalDateTime.now(),

    val location: String? = null,

    @Enumerated(EnumType.STRING)
    val status: TransactionStatus = TransactionStatus.PENDING,

    val deviceId: String? = null,

    val ipAddress: String? = null,

    val isFlagged: Boolean = false,

    val isDeposit: Boolean? = null,

    val isWithdrawal: Boolean? = null
)


enum class TransactionStatus {
    SUCCESS, PENDING
}
