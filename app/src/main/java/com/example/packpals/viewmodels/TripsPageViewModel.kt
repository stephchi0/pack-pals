package com.example.packpals.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Trip
import com.example.packpals.models.Pal
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PalsRepository
import com.example.packpals.repositories.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsPageViewModel @Inject constructor(private val authRepo: AuthRepository,
                                             private val palsRepo: PalsRepository,
                                             private val tripsRepo: TripsRepository): ViewModel() {

    private val _tripsList: MutableLiveData<List<Trip>> = MutableLiveData()
    val tripsList: LiveData<List<Trip>> get() = _tripsList

    private val _palsList: MutableLiveData<List<Pal>> = MutableLiveData()
    val palsList: LiveData<List<Pal>> get() = _palsList

    private val _currentTripPalIds: MutableLiveData<Set<String>> = MutableLiveData(emptySet())
    val currentTripPalIds: LiveData<Set<String>> get() = _currentTripPalIds

    fun fetchPalsList(){
        val userId = authRepo.getCurrentUID()
        viewModelScope.launch {
            val pal = userId?.let { palsRepo.fetchPal(it) }
            if (pal != null) {
                _palsList.value = listOf(Pal("id1",pal.name))
            }
            else{
                _palsList.value = listOf(Pal("id1","not yuh"))
            }
        }
        _palsList.value = listOf(Pal("id1","not not yuh"))
    }

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
        fetchPalsList()
    }

    fun editActive(trip: Trip){
        val tripId = trip.tripId
        if (tripId != null){
            val updatedTrip = Trip(trip.title,  trip.tripCreatorId,trip.tripPalIds,!trip.active!!,trip.tripId)
            viewModelScope.launch{
                tripsRepo.updateTrip(tripId,updatedTrip)
                fetchTrips()
            }
        }
    }

    fun getUserId(): String {
        return authRepo.getCurrentUID() ?: ""
    }

     fun fetchTrips() {
         val userId = authRepo.getCurrentUID()
         if (userId!=null){
             viewModelScope.launch {
                 val result = tripsRepo.fetchTrips(userId)
                 if (result != null) {
                     _tripsList.value = result!!
                 }
             }
         }
         fetchPalsList()
    }

    fun createTrip(title: String) {
        val tripPalIdsSet = _currentTripPalIds.value
        if (tripPalIdsSet != null) {
            viewModelScope.launch {
                val newTrip = Trip(title, authRepo.getCurrentUID(), tripPalIdsSet.toList(),true)
                tripsRepo.createTrip(newTrip)
                fetchTrips()
            }
        }
    }
}