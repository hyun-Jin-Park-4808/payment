package com.zerobase.payment.service

import com.zerobase.payment.TransactionType
import com.zerobase.payment.adapter.AccountAdapter
import com.zerobase.payment.adapter.CancelBalanceRequest
import com.zerobase.payment.adapter.UseBalanceRequest
import com.zerobase.payment.domain.Order
import com.zerobase.payment.exception.ErrorCode
import com.zerobase.payment.exception.PaymentException
import com.zerobase.payment.repository.OrderRepository
import com.zerobase.payment.repository.OrderTransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountAdapter: AccountAdapter,
    private val orderRepository: OrderRepository,
    private val orderTransactionRepository: OrderTransactionRepository
) {
    @Transactional
    fun useAccount(orderId: Long): String {
        // 계좌 사용 요청 및 처리
        val order: Order = orderRepository.findById(orderId)
            .orElseThrow { throw PaymentException(ErrorCode.ORDER_NOT_FOUND) }

        return accountAdapter.useAccount(
            UseBalanceRequest(
                userId = order.paymentUser.accountUserId,
                accountNumber = order.paymentUser.accountNumber,
                amount = order.orderAmount
            )
        ).transactionId
    }

    @Transactional
    fun cancelUseAccount(refundTxId: Long): String {
        val refundTransaction = orderTransactionRepository.findById(refundTxId)
            .orElseThrow {
                throw PaymentException(ErrorCode.INTERNAL_SERVER_ERROR)
            }

        // refundTx -> order -> paymentTx.payMethodTransactionId
        val order = refundTransaction.order
        val paymentTransaction = orderTransactionRepository.findByOrderAndTransactionType(
            order, TransactionType.PAYMENT
        ).first()

        return accountAdapter.cancelUseAccount(
            CancelBalanceRequest(
                transactionId = paymentTransaction.payMethodTransactionId
                    ?: throw PaymentException(ErrorCode.INTERNAL_SERVER_ERROR),
                accountId = order.paymentUser.accountNumber,
                amount = refundTransaction.transactionAmount
            )
        ).transactionId
    }
}