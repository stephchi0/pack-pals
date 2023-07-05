package com.example.packpals.models

data class TripPhoto(
    val imageUrl: String? = null,
    val tripId: String? = null,
    val uploaderId: String? = null,
    val location: Location? = null,
    val timestamp: Long? = null
)