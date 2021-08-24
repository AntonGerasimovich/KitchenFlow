package com.example.kitchenflow.data.entity

import java.util.*

abstract class BaseOrderModel {
    var orderId: String = ""
    var shortId: String = ""
}

data class TakeoutOrderInfoForDateModel(
    val pickupTime: String,
    val orderType: OrderType,
    val numOfItems: Int,
    val customers: List<Customer>,
    val scheduledFor: Date
) : BaseOrderModel() {
    val name: String
        get() = customers.first().name
    val isCA: Boolean
        get() = customers.first().isCA
}

data class PaymentInfoModel(
    val paymentStatus: PaymentStatus,
) : BaseOrderModel()

data class KitchenInfoModel(
    val orderStatus: OrderStatus,
    val preparationTimeSec: Int
) : BaseOrderModel()