package com.zerobase.payment.adapter

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDateTime

@FeignClient(
    name = "account-adapter",
    url = "http://localhost:8080",
)
interface AccountAdapter {
    @PostMapping("/transaction/use")
    fun useAccount(
        @RequestBody useBalanceRequest: UseBalanceRequest
    ): UseBalanceResponse

    @PostMapping("/transaction/cancel")
    fun cancelUseAccount(
        @RequestBody cancelBalanceRequest: CancelBalanceRequest
    ): CancelBalanceResponse
}

data class CancelBalanceResponse(
    val accountNumber: String,
    val transactionResult: TransactionResultType,
    val transactionId: String,
    val amount: Long,
    val transactedAt: LocalDateTime,
)

data class CancelBalanceRequest(
    val transactionId: String,
    val accountId: String,
    val amount: Long,
)

data class UseBalanceResponse(
    var accountNUmber: String,
    val transactionResult: TransactionResultType,
    val transactionId: String,
    val amount: Long,
    val transactedAt: LocalDateTime
)

enum class TransactionResultType {
    S, F
}

class UseBalanceRequest(
    val userId: Long? = null,
    val accountNumber: String? = null,
    val amount: Long? = null
)