package com.zerobase.payment.util

import java.util.*

fun generateOrderId() = "PO" + generateUUID()
fun generateTransactionId() = "PT" + generateUUID()
fun generateRefundId() = "RT" + generateUUID()

private fun generateUUID() = UUID.randomUUID().toString().replace("-", "")