package com.fraud.Transaction

data class TransferDto(
    val senderAccountId: Long,
    val receiverAccountId: Long,
    val amount: Double,
    val location: String?,
    val deviceId: String?,
    val ipAddress: String?
)