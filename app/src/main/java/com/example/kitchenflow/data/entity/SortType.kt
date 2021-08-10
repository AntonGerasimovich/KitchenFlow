package com.example.kitchenflow.data.entity

import android.content.res.Resources
import com.example.kitchenflow.R

enum class SortType {
    PREPARATION, PICKUP;
}

fun Resources.getSortTypeString(sortType: SortType): String {
    return when (sortType) {
        SortType.PREPARATION -> getString(R.string.sort_by_preparation_start)
        SortType.PICKUP -> getString(R.string.sort_by_pick_up_time)
    }
}