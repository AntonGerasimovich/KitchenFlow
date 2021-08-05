package com.example.kitchenflow.data.entity

import com.squareup.moshi.Json

sealed class BaseOrder(open val orderId: String) {
    data class TakeoutOrderForDate(
        @field:Json(name = "shortId")
        override val orderId: String,
        @field:Json(name = "pickupTime")
        val pickupTime: String,
        @field:Json(name = "type")
        val orderType: String,
        @field:Json(name = "numberOfItems")
        val numOfItems: Int,
        @field:Json(name = "customers")
        val customers: List<Customer>
    ): BaseOrder(orderId) {
        val name: String
            get() = customers.first().name
    }

    data class PaymentOrder(
        @field:Json(name = "shortId")
        override val orderId: String,
        @field:Json(name = "status")
        val paymentStatus: String,
    ): BaseOrder(orderId)

    data class KitchenOrder(
        @field:Json(name = "shortId")
        override val orderId: String,
        @field:Json(name = "status")
        val orderStatus: String
    ): BaseOrder(orderId)
}