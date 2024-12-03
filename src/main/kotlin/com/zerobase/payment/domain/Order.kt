package com.zerobase.payment.domain

import com.zerobase.payment.OrderStatus
import jakarta.persistence.*

@Entity
@Table(name = "orders")
class Order(
    val orderId: String,

    @ManyToOne
    val paymentUser: PaymentUser,

    @Enumerated(EnumType.STRING)
    var orderStatus: OrderStatus,

    val orderTitle: String, // 불변 값은 val
    val orderAmount: Long,
    var paidAmount: Long, // 변할 수 있는 값은 var
    var refundAmount: Long,
) : BaseEntity() // BaseEntity의 기본 생성자를 바탕으로 생성하겠다.