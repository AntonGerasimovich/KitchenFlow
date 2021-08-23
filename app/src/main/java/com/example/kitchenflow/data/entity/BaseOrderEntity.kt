package com.example.kitchenflow.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

abstract class BaseOrderEntity {
    @SerializedName("orderId")
    var orderId: String = ""

    @SerializedName("shortId")
    var shortId: String = ""

    abstract fun mapToModel(): BaseOrderModel
}

data class TakeoutOrderInfoForDateEntity(
    @SerializedName("pickupTime")
    val pickupTime: String,
    @SerializedName("type")
    val orderType: OrderTypeDTO,
    @SerializedName("numberOfItems")
    val numOfItems: Int,
    @SerializedName("customers")
    val customers: List<Customer>,
    @SerializedName("scheduledFor")
    val scheduledFor: Date
) : BaseOrderEntity() {
    val name: String
        get() = customers.first().name
    val isCA: Boolean
        get() = customers.first().isCA

    override fun mapToModel(): TakeoutOrderInfoForDateModel =
        TakeoutOrderInfoForDateModel(pickupTime, orderType.mapToModel(), numOfItems, customers, scheduledFor).apply {
            this.orderId = this@TakeoutOrderInfoForDateEntity.orderId
            this.shortId = this@TakeoutOrderInfoForDateEntity.shortId
        }
}

data class PaymentInfoEntity(
    @SerializedName("status")
    val paymentStatus: PaymentStatusDTO
) : BaseOrderEntity() {
    override fun mapToModel(): PaymentInfoModel = PaymentInfoModel(paymentStatus.mapToModel()).apply {
        this.orderId = this@PaymentInfoEntity.orderId
        this.shortId = this@PaymentInfoEntity.shortId
    }
}

data class KitchenInfoEntity(
    @SerializedName("status")
    val orderStatus: OrderStatusDTO,
    @SerializedName("preparationTime")
    val preparationTimeSec: Int
) : BaseOrderEntity() {
    override fun mapToModel(): KitchenInfoModel = KitchenInfoModel(orderStatus.mapToModel(), preparationTimeSec).apply {
        this.orderId = this@KitchenInfoEntity.orderId
    }
}