package com.fraud.transaction

import com.fraud.account.AccountEntity
import org.springframework.beans.factory.aot.AotServices.Source
import java.math.BigDecimal

data class TransferDto(
    val senderAccountId: String,
    val receiverAccountId: String,
    val amount: BigDecimal,
    val location: String?,
    val deviceId: String?,
    val ipAddress: String?
)

data class DepositWithdrawDto(
    val sourceAccount: String,
    val amount: BigDecimal,
    val location: String?,
    val deviceId: String?,
    val ipAddress: String?
)

data class WithdrawDto(
    val amount: BigDecimal,
    val location: String?,
    val deviceId: String?,
    val ipAddress: String?
)