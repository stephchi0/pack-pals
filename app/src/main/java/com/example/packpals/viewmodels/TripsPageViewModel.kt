package com.example.packpals.viewmodels

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

    private var _currentTripPalIds: MutableLiveData<Set<String>> = MutableLiveData(emptySet())
    val currentTripPalIds: LiveData<Set<String>> get() = _currentTripPalIds

    private var _currentEditTripItem: MutableLiveData<Trip> = MutableLiveData()
    val currentEditTripItem: LiveData<Trip> get() = _currentEditTripItem

    fun fetchPalsList(){
        val userId = authRepo.getCurrentUID()
        viewModelScope.launch {
            val pal = userId?.let { palsRepo.fetchPal(it) }
            if (pal?.pals != null) {
                val listOfPalIds = pal.pals!!
                val listOfPals = mutableListOf<Pal>()
                for (p in listOfPalIds){
                    val palItem = palsRepo.fetchPal(p)
                    if (palItem != null) {
                        listOfPals.add(palItem)
                    }
                }
                _palsList.value = listOfPals
            }
        }
    }

    fun addRemoveTripPal(palId: String) {
        if(_currentTripPalIds.value!!.contains(palId)){
            _currentTripPalIds.value = currentTripPalIds.value!!.minus(palId)
        }
        else{
            _currentTripPalIds.value = currentTripPalIds.value!!.plus(palId)
        }
    }

    init {
        fetchTrips()
        fetchPalsList()
    }

    fun editActive(trip: Trip){
        val tripId = trip.tripId
        if (tripId != null){
            val updatedTrip = Trip(trip.title,  trip.tripCreatorId,trip.tripPalIds,!trip.active!! ,trip.tripId)
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
    }

    fun clearTripItems(){
        _currentTripPalIds= MutableLiveData(emptySet())
    }

    fun editTrip(title:String){
        val currentEditTrip = _currentEditTripItem.value
        val tripPalIdsSet = _currentTripPalIds.value
        if (currentEditTrip != null && tripPalIdsSet != null) {
            viewModelScope.launch {
                val updatedTrip = currentEditTrip.copy(title = title, tripPalIds = tripPalIdsSet.toList())
                currentEditTrip.tripId?.let { tripsRepo.updateTrip(it, updatedTrip) }
                fetchTrips()
            }
        }
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

    fun selectTrip(selectedTrip: Trip) {
        tripsRepo.selectTrip(selectedTrip)
    }

    fun setCurrentEditTrip (trip: Trip){
        //set the trip and palslist
        _currentEditTripItem.value = trip
        _currentTripPalIds.value = trip.tripPalIds?.toSet()
    }

    fun getCurrentEditTrip(): Trip? {
        return currentEditTripItem.value
    }

    fun leaveTrip(userId: String, trip: Trip){
        viewModelScope.launch{
            val tripInfo = trip.tripId?.let { tripsRepo.fetchTrip(it) }
            if(tripInfo!=null){
                val creator = tripInfo.tripCreatorId
                if (creator==userId){
                    //reassign to a random trip pal if there are trip pals, else delete the trip
                    val tripPals = tripInfo.tripPalIds
                    if (tripPals != null) {
                        if(tripPals.isNotEmpty()){
                            val randomPal = tripPals.shuffled()[0]
                            //remove the random pal from the trip pals because now they're the creator

                            val newPals = tripPals.toMutableList()
                            newPals.remove(randomPal)
                            val updatedTrip = Trip(title = trip.title, tripCreatorId = randomPal,tripPalIds = newPals,active = trip.active!!,tripId = trip.tripId)
                            tripsRepo.updateTrip(trip.tripId!!, updatedTrip)
                            fetchTrips()
                        } else{
                            tripsRepo.deleteTrip(trip.tripId!!)
                            fetchTrips()
                        }
                    }
                }
                else{
                    //remove from trip pals list
                    val tripInfo = trip.tripId?.let { tripsRepo.fetchTrip(it) }
                    if(tripInfo!=null) {
                        val tripPals = tripInfo.tripPalIds
                        if (tripPals.isNullOrEmpty()){
                            tripsRepo.deleteTrip(trip.tripId!!)
                        }
                        val newPals = tripPals?.toMutableList()
                        newPals?.remove(userId)
                        val updatedTrip = Trip(trip.title,trip.tripCreatorId, newPals,trip.active,trip.tripId)
                        tripsRepo.updateTrip(trip.tripId!!, updatedTrip)
                    }
                    fetchTrips()
                }
            }
        }
    }

    fun logout() {
        authRepo.logout()
    }
}