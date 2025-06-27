package com.adorah.ExploroTravel.models

import android.icu.text.CaseMap

data class Booking(
    val id: String = "",
    val title: String = "",
    val destination: String = "",
    val price: Double = 0.0,
    val travelDate: String = "",
    val userId: String = ""
)
