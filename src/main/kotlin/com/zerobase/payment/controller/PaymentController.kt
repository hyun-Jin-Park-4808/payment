package com.zerobase.payment.controller

import com.zerobase.payment.service.PaymentService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RequestMapping("/api/v1")
@RestController
class PaymentController(
    private val paymentService: PaymentService
) {
    @PostMapping("/pay")
    fun pay(
        @Valid @RequestBody
        payRequest: PayRequest
    ): PayResponse {
        return PayResponse("p1", 100, "txId", LocalDateTime.now())
    }
}

class PayResponse(
    val payUserId: String,
    val amount: Long,
    val transactionId: String,
    val transactedAt: LocalDateTime,
)

data class PayRequest(
    @field: NotBlank // field 안써주면 생성자에 선언되는 값에 대해서만 notBlank 체크를 하고 field에 대해선 validation이 되지 않는다.
    val payUserId: String,
    @field: Min(100)
    val amount: Long,
    @field: NotBlank
    val merchantTransactionId: String,
    @field: NotBlank
    val orderTitle: String,
)