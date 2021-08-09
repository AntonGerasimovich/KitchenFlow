package com.example.kitchenflow.data.entity

import com.google.gson.annotations.SerializedName

abstract class BaseOrder {
    @SerializedName("orderId")
    var orderId: String = ""
    @SerializedName("shortId")
    var shortId: String = ""
}

data class TakeoutOrderForDate(
    @SerializedName("pickupTime")
    val pickupTime: String,
    @SerializedName("type")
    val orderType: String,
    @SerializedName("numberOfItems")
    val numOfItems: Int,
    @SerializedName("customers")
    val customers: List<Customer>
) : BaseOrder() {
    val name: String
        get() = customers.first().name
    val isCA: Boolean
        get() = customers.first().isCA
}

data class PaymentOrder(
    @SerializedName("status")
    val paymentStatus: String,
) : BaseOrder()

data class KitchenOrder(
    @SerializedName("status")
    val orderStatus: String
) : BaseOrder()