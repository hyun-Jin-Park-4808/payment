package com.zerobase.payment.domain

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "payment_user")
class PaymentUser(
    val payUserId: String,
    val accountUserId: Long,
    val accountNumber: String,
    val name: String,
) : BaseEntity()