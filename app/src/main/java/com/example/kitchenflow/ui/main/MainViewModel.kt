package com.example.kitchenflow.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitchenflow.data.entity.OrderModel
import com.example.kitchenflow.data.repository.KitchenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(private val repository: KitchenRepository) : ViewModel() {
    private val ordersData: MutableLiveData<List<OrderModel>> = MutableLiveData()
    val orders: LiveData<List<OrderModel>> = ordersData

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        repository.getAllOrders()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                ordersData.value = it
            }
    }
}