package com.example.kitchenflow.ui.main

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.kitchenflow.databinding.ItemOrderBinding

class OrdersAdapter(
    private val context: Context,
    private var orders: List<OrderModel> = mutableListOf()
) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdersViewHolder(binding)
    }

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
