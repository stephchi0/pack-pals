package com.example.packpals.models

import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class Itinerary_Item(
    val tripId: String? = null,
    val date: Date? = null,
    val forecast: String? = null,
    val geoPoint: GeoPoint? = null,
    val location: String? = null
)