package com.example.packpals.models

import java.util.Date

data class TripPhoto(
    val imageUrl: String? = null,
    val tripId: String? = null,
    val uploaderId: String? = null,
    val location: Location? = null,
    val timestamp: Date? = null
)