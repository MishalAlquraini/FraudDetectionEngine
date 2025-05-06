package com.fraud.Transaction

import java.math.BigDecimal

data class TransferDto(
    val senderAccountId: String,
    val receiverAccountId: String,
    val amount: BigDecimal,
    val location: String?,
    val deviceId: String?,
    val ipAddress: String?
)