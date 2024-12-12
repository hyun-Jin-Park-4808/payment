package com.zerobase.payment.domain

import com.zerobase.payment.TransactionStatus
import com.zerobase.payment.TransactionType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "order_transaction")
class OrderTransaction(
    val transactionId: String,

    @ManyToOne
    val order: Order,

    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType,

    @Enumerated(EnumType.STRING)
    var transactionStatus: TransactionStatus,

    val transactionAmount: Long,
    val merchantTransactionId: String,
    var payMethodTransactionId: String? = null,
    var transactedAt: LocalDateTime? = null,
    var failureCode: String? = null, // 실패될 때만 저장 됨.
    var description: String? = null,
) : BaseEntity()