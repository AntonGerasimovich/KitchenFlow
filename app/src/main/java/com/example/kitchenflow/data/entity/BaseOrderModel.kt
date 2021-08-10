package com.example.kitchenflow.data.entity

abstract class BaseOrderModel {
    var orderId: String = ""
    var shortId: String = ""
}

data class TakeoutOrderInfoForDateModel(
    val pickupTime: String,
    val orderType: OrderType,
    val numOfItems: Int,
    val customers: List<Customer>
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
    val orderStatus: OrderStatus
) : BaseOrderModel()