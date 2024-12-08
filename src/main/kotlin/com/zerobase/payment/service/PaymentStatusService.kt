package com.zerobase.payment.service

import com.zerobase.payment.OrderStatus
import com.zerobase.payment.TransactionStatus.RESERVE
import com.zerobase.payment.TransactionType.PAYMENT
import com.zerobase.payment.domain.Order
import com.zerobase.payment.domain.OrderTransaction
import com.zerobase.payment.exception.ErrorCode.INTERNAL_SERVER_ERROR
import com.zerobase.payment.exception.ErrorCode.INVALID_REQUEST
import com.zerobase.payment.exception.PaymentException
import com.zerobase.payment.repository.OrderRepository
import com.zerobase.payment.repository.OrderTransactionRepository
import com.zerobase.payment.repository.PaymentUserRepository
import com.zerobase.payment.util.generateOrderId
import com.zerobase.payment.util.generateTransactionId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 결제의 요청 저장, 성공, 실패 저장
 */
@Service
class PaymentStatusService(
    private val paymentUserRepository: PaymentUserRepository,
    private val orderRepository: OrderRepository,
    private val orderTransactionRepository: OrderTransactionRepository,
) {

    @Transactional
    fun savePayRequest(
        payUserId: String,
        amount: Long,
        orderTitle: String,
        merchantTransactionId: String,
    ): Long {
        // order, orderTransaction 저장
        val paymentUser = paymentUserRepository.findByPayUserId(payUserId)
            ?: throw PaymentException(INVALID_REQUEST, "사용자 없음: $payUserId")

        val order = orderRepository.save(
            Order(
                orderId = generateOrderId(),
                paymentUser = paymentUser,
                orderStatus = OrderStatus.CREATED,
                orderTitle = orderTitle,
                orderAmount = amount,
            )
        )

        orderTransactionRepository.save(
            OrderTransaction(
                transactionId = generateTransactionId(),
                order = order,
                transactionType = PAYMENT,
                transactionStatus = RESERVE,
                transactionAmount = amount,
                merchantTransactionId = merchantTransactionId,
                description = orderTitle
            )
        )
        return order.id ?: throw PaymentException(INTERNAL_SERVER_ERROR)
    }
}