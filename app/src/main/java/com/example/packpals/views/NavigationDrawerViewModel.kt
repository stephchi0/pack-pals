package com.example.packpals.views

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
class NavigationDrawerViewModel @Inject constructor(private val authRepo: AuthRepository,
                                                    private val palsRepo: PalsRepository,
                                                    private val tripsRepo: TripsRepository): ViewModel() {


    private val _tripPalsList: MutableLiveData<List<Pal>> = MutableLiveData()
    val tripPalsList: LiveData<List<Pal>> get() = _tripPalsList


    private val _currentTrip: MutableLiveData<Trip> = MutableLiveData()
    val currentTrip: LiveData<Trip> get() = _currentTrip

    fun fetchTripPalsList(){
        val userId =authRepo.getCurrentUID()
    }

    fun updateCurrentTrip(tripId: String){

    }

    fun fetchTrip(tripId: String){

    }
}