package com.example.kitchenflow.data.entity

data class VenueOpeningHours(
    val annualClosures: List<AnnualClosure>,
    val openingHours: List<OpeningHour>,
    val scheduledClosures: ScheduledClosures
)