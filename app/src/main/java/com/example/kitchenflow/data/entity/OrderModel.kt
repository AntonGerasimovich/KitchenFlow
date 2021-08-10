package com.example.kitchenflow.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class OrderModel(
    val orderId: String,
    val shortId: String,
    val paymentStatus: PaymentStatus,
    val username: String,
    val numOfItems: Int,
    val pickupTime: String,
    val orderType: OrderType,
    val orderStatus: OrderStatus,
    val isCA: Boolean
) : Parcelable

enum class PaymentStatus {
    Pending, Paid
}

private const val AUTHORIZED_DTO = "authorized"
private const val PAID_DTO = "captured"

enum class PaymentStatusDTO(val value: String) {
    @SerializedName(AUTHORIZED_DTO)
    Pending(AUTHORIZED_DTO),

    @SerializedName(PAID_DTO)
    Paid(PAID_DTO);

    fun mapToModel(): PaymentStatus = when (this) {
        Pending -> PaymentStatus.Pending
        Paid -> PaymentStatus.Paid
    }
}

enum class OrderType {
    Takeout, Delivery, Curbside
}

private const val TAKEOUT_DTO = "takeout"
private const val DELIVERY_DTO = "delivery"
private const val CURBSIDE_DTO = "curbside"

enum class OrderTypeDTO(val value: String) {
    @SerializedName(TAKEOUT_DTO)
    Takeout(TAKEOUT_DTO),

    @SerializedName(DELIVERY_DTO)
    Delivery(DELIVERY_DTO),

    @SerializedName(CURBSIDE_DTO)
    Curbside(CURBSIDE_DTO);

    fun mapToModel(): OrderType = when (this) {
        Takeout -> OrderType.Takeout
        Delivery -> OrderType.Delivery
        Curbside -> OrderType.Curbside
    }
}

private const val PENDING = "pending"
private const val FIRED = "fired"
private const val READY_FOR_DELIVERY = "ready_for_delivery"
private const val OUT_FOR_DELIVERY = "out_for_delivery"
private const val DELIVERED = "delivered"
private const val CANCELED = "canceled"
private const val DELETED = "deleted"
private const val READY_FOR_PICKUP = "ready_for_pickup"
private const val IN_PROGRESS = "in_progress"

enum class OrderStatusDTO(val value: String) {
    @SerializedName(PENDING)
    Pending(PENDING),

    @SerializedName(FIRED)
    Fired(FIRED),

    @SerializedName(READY_FOR_DELIVERY)
    ReadyForDelivery(READY_FOR_DELIVERY),

    @SerializedName(OUT_FOR_DELIVERY)
    OutForDelivery(OUT_FOR_DELIVERY),

    @SerializedName(DELIVERED)
    Delivered(DELIVERED),

    @SerializedName(CANCELED)
    Canceled(CANCELED),

    @SerializedName(DELETED)
    Deleted(DELETED),

    @SerializedName(READY_FOR_PICKUP)
    ReadyForPickup(READY_FOR_PICKUP),

    @SerializedName(IN_PROGRESS)
    InProgress(IN_PROGRESS);

    fun mapToModel(): OrderStatus = when (this) {
        Pending -> OrderStatus.Pending
        Fired -> OrderStatus.Fired
        ReadyForDelivery -> OrderStatus.ReadyForDelivery
        OutForDelivery -> OrderStatus.OutForDelivery
        Delivered -> OrderStatus.Delivered
        Canceled -> OrderStatus.Canceled
        Deleted -> OrderStatus.Deleted
        ReadyForPickup -> OrderStatus.ReadyForPickup
        InProgress -> OrderStatus.InProgress
    }
}

enum class OrderStatus {
    Pending,
    Fired,
    ReadyForDelivery,
    OutForDelivery,
    Delivered,
    Canceled,
    Deleted,
    ReadyForPickup,
    InProgress
}

fun String.convertToNormalDate(): String {
    return if (this.isNotEmpty()) {
        val inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val date = LocalDateTime.parse(this, inputFormatter)
        outputFormatter.format(date)
    } else ""
}
