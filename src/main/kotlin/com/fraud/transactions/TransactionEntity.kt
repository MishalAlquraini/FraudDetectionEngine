package com.fraud.Transaction

import com.fraud.account.AccountEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sender_account_id", referencedColumnName = "id")
    val senderAccount: AccountEntity,

    @ManyToOne
    @JoinColumn(name = "receiver_account_id", referencedColumnName = "id")
    val receiverAccount: AccountEntity,

    val amount: Double,

    val timestamp: LocalDateTime = LocalDateTime.now(),

    val location: String? = null,

    @Enumerated(EnumType.STRING)
    val status: TransactionStatus = TransactionStatus.PENDING,

    val deviceId: String? = null,

    val ipAddress: String? = null,

    val isFlagged: Boolean = false,

    val deposit: Boolean? = null,

    val withdrawal: Boolean? = null
)

enum class TransactionStatus {
    SUCCESS, PENDING
}
