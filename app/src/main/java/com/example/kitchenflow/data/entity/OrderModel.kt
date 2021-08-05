package com.example.kitchenflow.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Parcelize
data class OrderModel(
    val orderId: String,
    val paymentStatus: PaymentStatus,
    val username: String,
    val numOfItems: Int,
    val pickupTime: String,
    val orderType: OrderType,
    val orderStatus: OrderStatus
) : Parcelable

enum class PaymentStatus(val status: String) {
    Pending("authorized"), Paid("captured")
}

fun String.getPaymentStatus(): PaymentStatus = when (this) {
    PaymentStatus.Pending.status -> PaymentStatus.Pending
    PaymentStatus.Paid.status -> PaymentStatus.Paid
    else -> PaymentStatus.Pending //по хорошему заменить бы на проброс исключения
}

enum class OrderType(val type: String) {
    Takeout("takeout"), Delivery("delivery"), Curbside("curbside")
}

fun String.getOrderType(): OrderType = when (this) {
    OrderType.Takeout.type -> OrderType.Takeout
    OrderType.Delivery.type -> OrderType.Delivery
    OrderType.Curbside.type -> OrderType.Curbside
    else -> OrderType.Delivery
}

enum class OrderStatus(val status: String) {
    Pending("pending"),
    Fired("fired"),
    ReadyForDelivery("ready_for_delivery"),
    OutForDelivery("out_for_delivery"),
    Delivered("delivered"),
    Canceled("canceled"),
    Deleted("deleted"),
    ReadyForPickup("ready_for_pickup"),
    InProgress("in_progress")
}

fun String.getOrderStatus(): OrderStatus = when (this) {
    OrderStatus.Pending.status -> OrderStatus.Pending
    OrderStatus.Fired.status -> OrderStatus.Fired
    OrderStatus.ReadyForDelivery.status -> OrderStatus.ReadyForDelivery
    OrderStatus.OutForDelivery.status -> OrderStatus.OutForDelivery
    OrderStatus.Delivered.status -> OrderStatus.Delivered
    OrderStatus.Canceled.status -> OrderStatus.Canceled
    OrderStatus.Deleted.status -> OrderStatus.Deleted
    OrderStatus.ReadyForPickup.status -> OrderStatus.ReadyForPickup
    else -> OrderStatus.Canceled
}

fun String.convertToNormalDate(): String {
    return if (this.isNotEmpty()) {
        val inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:SSS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("MMM d")
        val date = LocalDate.parse(this, inputFormatter)
        outputFormatter.format(date)
    } else ""
}
