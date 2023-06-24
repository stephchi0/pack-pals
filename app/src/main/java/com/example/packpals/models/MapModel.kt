package com.example.packpals.models

import android.icu.util.GregorianCalendar
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableArrayList
import kotlin.math.sign

class MapModel {

    data class ModelLocation(
        val id: Int,
        var title: String,
        var content: String,
        var visited: Boolean = false
    ) : BaseObservable() {
        val timestamp = GregorianCalendar.getInstance().time.time
    }

    val map = ObservableArrayList<ModelLocation>()
}
