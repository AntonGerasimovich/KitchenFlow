package com.example.kitchenflow.data.entity

import com.squareup.moshi.Json

data class Customer(
    @field:Json(name = "name")
    val name: String
)