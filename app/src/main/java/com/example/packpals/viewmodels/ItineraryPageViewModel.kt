package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Itinerary_Item
import com.example.packpals.repositories.ItineraryRepository
import com.example.packpals.repositories.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class ItineraryPageViewModel @Inject constructor(
    private val tripsRepo: TripsRepository,
    private val itineraryRepo: ItineraryRepository
) : ViewModel() {

    private val _itineraryItemsList: MutableLiveData<List<Itinerary_Item>> = MutableLiveData(
        emptyList()
    )
    private val _itineraryItemsInfoList: MutableLiveData<List<Itinerary_Item>> = _itineraryItemsList
    val itineraryItemsInfoList: LiveData<List<Itinerary_Item>> = _itineraryItemsInfoList

    private val _reccItineraryItemsList: MutableLiveData<MutableList<Itinerary_Item>> = MutableLiveData()

    val reccItineraryItemsList: LiveData<MutableList<Itinerary_Item>> get() = _reccItineraryItemsList


    init {
        _itineraryItemsList.value = mutableListOf(
            Itinerary_Item("Fenugs Chasm", "May 1st, 2023 12:30pm-2pm", "Forecast: Rainy, 19 degrees Celsius"),
            Itinerary_Item("Anzac Hill of Chi", "May 2nd, 2023 2pm-5pm", "Forecast: Partly Cloudy, 19 degrees Celsius"),
            Itinerary_Item("Mount Spooder", "May 3rd, 2023 2pm-5pm", "Forecast: Sunny, 19 degrees Celsius")
        )
    }

    init {
        _reccItineraryItemsList.value = mutableListOf(
            Itinerary_Item("Chasm of chi", "", ""),
            Itinerary_Item("Fengus Hill", "", ""),
            Itinerary_Item("Across the Spooder Cave", "", "")
        )
    }

    fun fetchItineraryItems() {
        val tripId = "for later"
        if (tripId != null) {
            viewModelScope.launch {
                val result = itineraryRepo.fetchItems(tripId)
                if (result != null) {
                    _itineraryItemsList.value = result!!
                    createItemCardInfo()
                }
            }
        }
    }

    fun createItemCardInfo(){
        val newItemCardInfoList = mutableListOf<Itinerary_Item>()
    }

    fun addItineraryItem(item: Itinerary_Item) {
        val currentList = _itineraryItemsList.value?.toMutableList()
        currentList?.add(item)
        _itineraryItemsList.value = currentList!!
    }

    fun removeItineraryItem(item: Itinerary_Item) {
        val currentList = _itineraryItemsList.value?.toMutableList()
        currentList?.remove(item)
        _itineraryItemsList.value = currentList!!
    }

    fun addReccItineraryItem(item: Itinerary_Item) {
        val currentList = _reccItineraryItemsList.value?.toMutableList()
        currentList?.add(item)
        _reccItineraryItemsList.value = currentList!!
    }

    fun removeReccItineraryItem(item: Itinerary_Item) {
        val currentList = _reccItineraryItemsList.value?.toMutableList()
        currentList?.remove(item)
        _reccItineraryItemsList.value = currentList!!
    }
}
