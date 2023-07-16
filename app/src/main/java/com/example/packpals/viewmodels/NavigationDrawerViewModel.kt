package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Trip
import com.example.packpals.models.Pal
import com.example.packpals.repositories.PalsRepository
import com.example.packpals.repositories.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class NavigationDrawerViewModel @Inject constructor(private val palsRepo: PalsRepository,
                                                         private val tripsRepo: TripsRepository): ViewModel() {


    private val _tripPalsList: MutableLiveData<List<Pal>> = MutableLiveData()
    val tripPalsList: LiveData<List<Pal>> get() = _tripPalsList


    private val _currentTrip: MutableLiveData<Trip?> = MutableLiveData()
    val currentTrip: MutableLiveData<Trip?> get() = _currentTrip

    fun fetchTripPalsList(){
        val tripPalIds = _currentTrip.value?.tripPalIds
        if(tripPalIds!=null){
            val tripPals = mutableListOf<Pal>()
            if (tripPalIds != null) {
                viewModelScope.launch{
                    for (pal in tripPalIds){
                        val palItem = palsRepo.fetchPal(pal)
                        if(palItem!=null){
                            tripPals.add(palItem)
                        }

                    }
                }
            }
            _tripPalsList.value = tripPals
        }
        else{
            _tripPalsList.value = mutableListOf()
        }
    }
    fun updateCurrentTrip(tripId: String){
        viewModelScope.launch{
            val trip = tripsRepo.fetchTrip(tripId)
            _currentTrip.value = trip
        }
    }
}