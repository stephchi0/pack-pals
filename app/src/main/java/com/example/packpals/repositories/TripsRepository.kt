package com.example.packpals.repositories

import com.example.packpals.models.Trip
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TripsRepository @Inject constructor (private val tripsCollectionRef: CollectionReference) {
    suspend fun fetchTrips(): List<Trip>? {
        return try {
            tripsCollectionRef.get().await().toObjects(Trip::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createTrip(trip: Trip) {
        tripsCollectionRef.add(trip).await()
    }
}