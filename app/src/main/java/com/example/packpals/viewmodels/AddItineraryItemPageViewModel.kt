package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Itinerary_Item

class AddItineraryItemPageViewModel: ViewModel() {
    private val _addItineraryItemsList: MutableLiveData<List<Itinerary_Item>> = MutableLiveData()

    val addItineraryItemsList: LiveData<List<Itinerary_Item>> get() = _addItineraryItemsList
    init {
        _addItineraryItemsList.value = mutableListOf(
            Itinerary_Item("Chasm of chi", "", ""),
            Itinerary_Item("Fengus Hill", "", ""),
            Itinerary_Item("Across the Spooder Cave", "", "")
        )
    }

    fun addItem(item: Itinerary_Item) {
        val currentList = _addItineraryItemsList.value?.toMutableList() // Retrieve the current list
        currentList?.add(item) // Add the new item to the list
        _addItineraryItemsList.value = currentList!!
    }

    fun removeItem(item: Itinerary_Item) {
        // TODO
    }
}