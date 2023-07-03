package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Trip
import com.example.packpals.models.Pal
import com.example.packpals.repositories.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsPageViewModel @Inject constructor(private val tripsRepo: TripsRepository): ViewModel() {
    private val _tripsList: MutableLiveData<List<Trip>> = MutableLiveData()
    val tripsList: LiveData<List<Trip>> get() = _tripsList

    val palsList: MutableLiveData<List<Pal>> = MutableLiveData(
        listOf(
            Pal("id1", "stooge"),
            Pal("id2", "mooge"),
            Pal("id3", "looge"),
            Pal("id4", "jooge")
        )
    )

    private val _currentTripPalIds: MutableLiveData<Set<String>> = MutableLiveData(emptySet())
    val currentTripPalIds: LiveData<Set<String>> get() = _currentTripPalIds

    fun addRemoveTripPal(palId: String) {
        val newTripPalIds = _currentTripPalIds.value!!.toMutableSet()
        if (palId in newTripPalIds) {
            newTripPalIds.remove(palId)
        }
        else {
            newTripPalIds.add(palId)
        }

        _currentTripPalIds.value = newTripPalIds
    }

    init {
        fetchTrips()
    }


    private fun fetchTrips() {
        viewModelScope.launch {
            val result = tripsRepo.fetchTrips()
            if (result != null) {
                _tripsList.value = result!!
            }
        }
    }

    fun createTrip(title: String) {
        val tripPalIdsSet = _currentTripPalIds.value
        if (tripPalIdsSet != null) {
            viewModelScope.launch {
                val newTrip = Trip(title, "creatorid", tripPalIdsSet.toList())
                tripsRepo.createTrip(newTrip)
                fetchTrips()
            }
        }
    }
}