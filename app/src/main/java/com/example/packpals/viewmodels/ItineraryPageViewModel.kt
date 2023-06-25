package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Itinerary_Item

class ItineraryPageViewModel : ViewModel() {
    private val _itineraryItemsList: MutableLiveData<List<Itinerary_Item>> = MutableLiveData()
    val itineraryItemsList: LiveData<List<Itinerary_Item>> get() = _itineraryItemsList

    init {
        _itineraryItemsList.value = mutableListOf(
            Itinerary_Item("Fenugs Chasm", " May 1st, 2023 12:30pm-2pm", "Forecast: Rainy, 19 degrees Celsius"),
            Itinerary_Item("Anzac Hill of Chi", "May 2nd, 2023 2pm-5pm", "Forecast: Partly Cloudy, 19 degrees Celsius"),
            Itinerary_Item("Mount Spooder", "May 3rd, 2023 2pm-5pm", "Forecast: Sunny, 19 degrees Celsius")
        )
    }

    fun addItem(item: Itinerary_Item) {
        // TODO
    }

    fun removeItem(item: Itinerary_Item) {
        // TODO
    }
}