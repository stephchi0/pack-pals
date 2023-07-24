package com.example.packpals.models

import com.google.firebase.firestore.GeoPoint
import java.time.LocalDate
import java.util.Date

data class Itinerary_Item(
    var tripId: String? = null,
    var startDate: Date? = null,
    var endDate: Date? = null,
    var geopoint: GeoPoint? = null,
    var location: String? = null,
    var forecast: String? = null,
    var photo_reference: String? = null,
    var address: String? = null,
    var itemId: String? = null
){
    fun addForecast(weather: String) {
        this.forecast = weather
    }

    fun addPhotoReference(id: String) {
        this.photo_reference = id
    }

    fun addGeoPoint(geo: GeoPoint){
        this.geopoint = geo
    }

    fun addLocation(s: String){
        this.location = s
    }

    fun addTripId(s: String){
        this.tripId = s
    }

    fun addAddress(s: String){
        this.address = s
    }
    fun addStartDate(d: Date){
        this.startDate = d
    }

    fun addEndDate(d:Date){
        this.endDate = d
    }
}

