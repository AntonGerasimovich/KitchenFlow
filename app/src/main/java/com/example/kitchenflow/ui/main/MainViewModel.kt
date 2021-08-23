package com.example.kitchenflow.ui.main

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitchenflow.data.entity.OrderModel
import com.example.kitchenflow.data.entity.VenueOpeningHours
import com.example.kitchenflow.data.repository.KitchenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel(private val repository: KitchenRepository) : ViewModel() {
    private val ordersData: MutableLiveData<List<OrderModel>> = MutableLiveData()
    val orders: LiveData<List<OrderModel>> = ordersData
    var date: MutableLiveData<Date> = MutableLiveData(
        getCurrentDate()
    )
    lateinit var venueOpeningHours: VenueOpeningHours
    val dateAndTime: ObservableField<String> = ObservableField(
        SimpleDateFormat("EEE, MMMM dd", Locale.ENGLISH).format(
            date.value?.time
        )
    )

    private fun getCurrentDate() = Calendar.getInstance().time

    val currentOrders: ObservableField<String> = ObservableField("0")

    init {
        getVenueOpeningHours()
        fetchOrders()
    }

    private fun getVenueOpeningHours() {
        repository.getVenueOpeningHours()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                venueOpeningHours = it
            }
    }

    @SuppressLint("SimpleDateFormat")
    fun isRestaurantOpen(): Boolean {
        var calendar = Calendar.getInstance()
        date.value?.month?.let { calendar.set(Calendar.MONTH, it) }
        val annualClosure =
            venueOpeningHours.annualClosures.find {
                it.date == "${
                    SimpleDateFormat("MMMM").format(calendar.time).uppercase()
                }/${date.value?.date}"
            }

        val scheduledClosuresFrom = venueOpeningHours.scheduledClosures.period.from
        val scheduledClosuresTo = venueOpeningHours.scheduledClosures.period.to

        val daysOfWeek = venueOpeningHours.openingHours
        calendar = Calendar.getInstance()
        date.value?.day?.let { calendar.set(Calendar.DAY_OF_WEEK, it + 1) }
        val openDay = daysOfWeek.find {
            it.daysOfWeek.contains(
                SimpleDateFormat("EEEE").format(calendar.time).uppercase()
            )
        }
        return if (annualClosure != null && annualClosure.closedAllDay) {
            false
        } else if (date.value?.time in scheduledClosuresFrom.time..scheduledClosuresTo.time) {
            true
        } else {
            openDay != null
        }
    }

    private fun fetchOrders() {
        repository.getAllOrders()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                ordersData.postValue(it)
            }
    }

    fun sortItems(sortString: String): List<OrderModel>? {
        return orders.value?.filter { sortString in it.username || sortString in it.shortId }
    }

    fun nextDay(view: View) {
        date.value?.time?.plus(TimeUnit.DAYS.toMillis(1))?.let { date.value?.time = it }
        dateAndTime.set(
            SimpleDateFormat("EEE, MMMM dd", Locale.ENGLISH).format(
                date.value?.time
            )
        )
        fetchOrders()
    }

    fun previousDay(view: View) {
        date.value?.time?.minus(TimeUnit.DAYS.toMillis(1))?.let { date.value?.time = it }
        dateAndTime.set(
            SimpleDateFormat("EEE, MMMM dd", Locale.ENGLISH).format(
                date.value?.time
            )
        )
        fetchOrders()
    }

    fun updateDate(date: Date) {
        this.date.value = date
        dateAndTime.set(
            SimpleDateFormat("EEE, MMMM dd", Locale.ENGLISH).format(
                this.date.value?.time
            )
        )
        fetchOrders()
    }
}