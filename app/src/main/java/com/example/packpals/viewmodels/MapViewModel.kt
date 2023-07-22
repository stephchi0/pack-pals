package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.packpals.models.MapModel
import com.google.firebase.firestore.GeoPoint
import java.util.Date

class MapViewModel : ViewModel() {

    private val _locationLiveData = MutableLiveData(listOf(
        MapModel.ModelLocation("100001","101", Date(2022,1,1,1,1),
            "Sunny", GeoPoint(37.42242770588777, -122.0845403133058), "Location 1"
        ),
        MapModel.ModelLocation("100001","102", Date(2022,9,1,1,1),
            "Not Sunny",GeoPoint(37.42152394191977, -122.08290410837543), "Location 2")
    ))

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel>
                    create(modelClass: Class<T>, extras: CreationExtras): T {
                return MapViewModel() as T
            }
        }
    }

    val locationLiveData: LiveData<List<MapModel.ModelLocation>>
        get() = _locationLiveData

}