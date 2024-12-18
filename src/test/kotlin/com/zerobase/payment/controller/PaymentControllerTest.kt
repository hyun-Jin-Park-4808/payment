package com.zerobase.payment.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.zerobase.payment.service.PayServiceResponse
import com.zerobase.payment.service.PaymentService
import io.mockk.every
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

@WebMvcTest(PaymentController::class)
class PaymentControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    @MockkBean
    private lateinit var paymentService: PaymentService

    private val mapper = ObjectMapper()

    @Test
    fun `결제 요청 - 성공 응답`() {
        // given
        every {
            paymentService.pay(any())
        } returns PayServiceResponse(
            payUserId = "p1",
            amount = 200,
            transactionId = "transactionId",
            transactedAt = LocalDateTime.now()
        )
        // when
        // then
        mockMvc.post("/api/v1/pay") {
            this.contentType = MediaType.APPLICATION_JSON
            headers {
                this.accept = listOf(APPLICATION_JSON)
            }
            content = mapper.writeValueAsString(
                PayRequest(
                    payUserId = "p1",
                    amount = 100,
                    merchantTransactionId = "m1",
                    orderTitle = "orderTitle"
                )
            )
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.payUserId", equalTo("p1")) }
            content { jsonPath("$.amount", equalTo(200)) }
            content { jsonPath("$.transactionId", equalTo("transactionId")) }
        }.andDo { print() }
    }
}

