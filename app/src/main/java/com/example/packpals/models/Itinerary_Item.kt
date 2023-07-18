package com.example.packpals.models

import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class Itinerary_Item(
    val tripId: String? = null,
    val date: Date? = null,
    var forecast: String? = null,
    val geopoint: GeoPoint? = null,
    val location: String? = null
){
    fun addForecast(weather: String) {
        this.forecast = weather
    }
}

