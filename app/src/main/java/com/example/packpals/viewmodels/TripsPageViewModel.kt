package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Trip
import com.example.packpals.models.Pal
import com.google.firebase.firestore.FirebaseFirestore

class TripsPageViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val tripsCollectionRef = db.collection("trips")

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
        tripsCollectionRef.get().addOnSuccessListener { result ->
            val newTripsList = mutableListOf<Trip>()
            for (trip in result.documents) {
                val tripObject = trip.toObject(Trip::class.java)
                if (tripObject != null) {
                    newTripsList.add(tripObject)
                }
            }
            _tripsList.value = newTripsList
        }
    }

    fun createTrip(title: String) {
        val newTrip = Trip(title, "creatorid", _currentTripPalIds.value!!.toList())
        tripsCollectionRef.add(newTrip)
        fetchTrips()
    }
}