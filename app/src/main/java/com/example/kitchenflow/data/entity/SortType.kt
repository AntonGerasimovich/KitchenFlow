package com.example.kitchenflow.data.entity

enum class SortType(val sortString: String) {
    // Хардкод конечно не очень, но пока оставим так
    PREPARATION("Sort by Preparation Start Time"), PICKUP("Sort by Pick up Time");

    companion object {
        fun getSortTypeByString(sortString: String): SortType {
            return when (sortString) {
                PREPARATION.sortString -> PREPARATION
                PICKUP.sortString -> PICKUP
                else -> PREPARATION
            }
        }
    }
}