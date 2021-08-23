package com.example.kitchenflow.data.entity

data class AnnualClosure(
    val closedAllDay: Boolean,
    var date: String,
    val holidayId: String,
    val name: String,
    val type: String
)