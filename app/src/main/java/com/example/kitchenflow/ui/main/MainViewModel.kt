package com.example.kitchenflow.ui.main

import android.icu.util.Calendar
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitchenflow.data.entity.OrderModel
import com.example.kitchenflow.data.repository.KitchenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(private val repository: KitchenRepository) : ViewModel() {
    private val ordersData: MutableLiveData<List<OrderModel>> = MutableLiveData()
    val orders: LiveData<List<OrderModel>> = ordersData
    val date: ObservableField<String> = ObservableField(
        SimpleDateFormat("EEE, MMMM dd", Locale.ENGLISH).format(
            Calendar.getInstance().time
        )
    )
    val currentOrders: ObservableField<String> = ObservableField("0")

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        repository.getAllOrders()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                ordersData.postValue(it)
                currentOrders.set(it.size.toString())
            }
    }

    fun sortItems(sortString: String): List<OrderModel>? {
        return orders.value?.filter { sortString in it.username || sortString in it.shortId }
    }
}