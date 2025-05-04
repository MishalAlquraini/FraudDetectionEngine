package com.fraud.FraudDetectionEngine.fraudFlag

import com.fraud.FraudDetectionEngine.transactions.TransactionEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "fraud_flags")
data class FraudFlagEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    val transaction: TransactionEntity,

    val reason: String,

    @Enumerated(EnumType.STRING)
    val severity: SeverityLevel,

    val reviewedByAdmin: Boolean = false,

    @Enumerated(EnumType.STRING)
    val finalStatus: FinalStatus? = null,

    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class SeverityLevel {
    LOW, MEDIUM, HIGH
}

enum class FinalStatus {
    TRUE, FALSE
    // NULL is not used as an enum constant because the field being nullable already allows null
}
