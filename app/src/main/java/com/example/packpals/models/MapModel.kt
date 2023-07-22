package com.example.packpals.models

import android.icu.util.GregorianCalendar
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableArrayList
import com.google.firebase.firestore.GeoPoint
import java.time.LocalDateTime
import java.util.Date
import kotlin.math.sign

class MapModel {

    data class ModelLocation(
        val tripId: String? = null,
        val itineraryId: String? = null,
        val date: Date? = null,
        var forecast: String? = null,
        val geopoint: GeoPoint? = null,
        val location: String? = null

        ) : BaseObservable() {
        val timestamp = GregorianCalendar.getInstance().time.time
    }



    val map = ObservableArrayList<ModelLocation>()
}
