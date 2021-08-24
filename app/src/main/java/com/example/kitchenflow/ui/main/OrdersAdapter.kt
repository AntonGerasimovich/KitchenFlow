package com.example.kitchenflow.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.kitchenflow.R
import com.example.kitchenflow.data.entity.OrderModel
import com.example.kitchenflow.data.entity.OrderStatus
import com.example.kitchenflow.data.entity.OrderType
import com.example.kitchenflow.data.entity.SortType
import com.example.kitchenflow.databinding.ItemOrderBinding
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime

class OrdersAdapter(
    private val context: Context,
    private var orders: List<OrderModel> = mutableListOf()
) : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    companion object {
        private const val KITCHEN_PREP_MILLIS: Long = 15 * 1000 * 60
        private const val MIN_DRIVE_TIME_MILLIS: Long = 30 * 1000 * 60
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdersViewHolder(binding)
    }

    @ExperimentalTime
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        with(holder.binding) {
            val order = orders[position]
            this.order = order
            setUpMenu(order)
            orderTypeIv.setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    when (order.orderType) {
                        OrderType.Takeout -> R.drawable.ic_takeout
                        OrderType.Delivery -> R.drawable.ic_delivery
                        OrderType.Curbside -> R.drawable.ic_car
                    }
                )
            )
            setUpTimer(order)
            if (order.isCA) {
                labelIv.visibility = View.VISIBLE
                orderTypeTv.visibility = View.GONE
            } else {
                labelIv.visibility = View.GONE
                orderTypeTv.visibility = View.VISIBLE
                orderTypeTv.text = order.orderType.name
            }
        }
    }

    @ExperimentalTime
    private fun ItemOrderBinding.setUpTimer(order: OrderModel) {
        fun calculateEstimateTime() {
            val currentTime = Instant.ofEpochMilli(System.currentTimeMillis())
            var orderPrepTime =
                Instant.ofEpochMilli(order.scheduledFor.time - order.preparationTimeSec.toLong() + KITCHEN_PREP_MILLIS)

            if (order.orderType == OrderType.Curbside || order.orderType == OrderType.Takeout) {
                showEstimateTime(currentTime, orderPrepTime, order)
            } else {
                orderPrepTime = orderPrepTime.minusMillis(
                    Instant.ofEpochMilli(MIN_DRIVE_TIME_MILLIS).toEpochMilli()
                )
                showEstimateTime(currentTime, orderPrepTime, order)
            }
        }

        calculateEstimateTime()
        object : CountDownTimer(TimeUnit.MINUTES.toMillis(1), TimeUnit.MINUTES.toMillis(1)) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                calculateEstimateTime()
                setUpTimer(order)
            }
        }.start()
    }

    @ExperimentalTime
    private fun ItemOrderBinding.showEstimateTime(
        currentTime: Instant,
        orderPrepTime: Instant,
        order: OrderModel
    ) {
        ioTimerTv.text = context.getString(
            R.string.time_minutes, if (currentTime >= orderPrepTime) {
                ioTimerTv.setTextColor(context.getColor(R.color.colorPrimaryRed))
                order.timer = TimeUnit.MILLISECONDS.toMinutes(
                    (currentTime.minusMillis(orderPrepTime.toEpochMilli())).toEpochMilli()
                ).toInt()
                "+${if (order.timer >= 99) 99 else order.timer}m"
            } else {
                TimeUnit.MILLISECONDS.toMinutes(
                    (orderPrepTime.minusMillis(currentTime.toEpochMilli()).toEpochMilli())
                ).toString()
            }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sort(sortType: SortType) {
        orders = when (sortType) {
            SortType.PREPARATION -> {
                orders.sortedBy { it.preparationTimeSec }
            }
            SortType.PICKUP -> {
                orders.sortedBy { it.scheduledFor }
            }
        }
        notifyDataSetChanged()
    }

    private fun ItemOrderBinding.setUpMenu(order: OrderModel) {
        val pMenu: Int
        when (order.orderStatus) {
            OrderStatus.InProgress -> {
                pMenu = R.menu.menu_in_progress
                indicator.setBackgroundColor(context.getColor(R.color.colorGreen))
            }
            OrderStatus.ReadyForPickup -> {
                pMenu = R.menu.menu_ready_for_pickup
                indicator.setBackgroundColor(context.getColor(R.color.colorSkyBlue))
            }
            OrderStatus.OutForDelivery -> {
                pMenu = R.menu.menu_out_for_delivery
                indicator.setBackgroundColor(context.getColor(R.color.purple_200))
            }
            OrderStatus.Delivered -> {
                pMenu = R.menu.menu_delivered
                indicator.setBackgroundColor(context.getColor(R.color.colorLightGrey))
            }
            OrderStatus.Canceled -> {
                pMenu = R.menu.menu_canceled
                indicator.setBackgroundColor(context.getColor(R.color.colorSecondaryRed))
            }
            else -> {
                pMenu = R.menu.menu_canceled
                indicator.setBackgroundColor(context.getColor(R.color.colorSecondaryRed))
            }
        }
        val popupMenu = PopupMenu(context, menu)
        popupMenu.inflate(pMenu)
        menu.setOnClickListener {
            popupMenu.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(orders: List<OrderModel>) {
        this.orders = orders
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = orders.size

    inner class OrdersViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)
}
