package com.zerobase.payment

enum class OrderStatus {
    CREATED,
    FAILED,
    PAID,
    CANCELLED,
    PARTIAL_REFUNDED,
    REFUNDED
}

enum class TransactionType {
    PAYMENT, REFUND, CANCEL
}

enum class TransactionStatus { // 예약, 성공, 실패
    RESERVE, SUCCESS, FAILURE
}