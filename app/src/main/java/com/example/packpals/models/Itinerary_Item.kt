package com.example.packpals.models

import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class Itinerary_Item(
    val tripId: String? = null,
    val date: Date? = null,
    val geopoint: GeoPoint? = null,
    val location: String? = null,
    var forecast: String? = null,
    var photo_reference: String? = null
){
    fun addForecast(weather: String) {
        this.forecast = weather
    }

    fun addPhotoReference(id: String) {
        this.photo_reference = id
    }
}

