package com.example.packpals.repositories

import com.example.packpals.models.Pal
import com.example.packpals.models.Trip
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TripsRepository @Inject constructor (private val tripsCollectionRef: CollectionReference) {
    suspend fun fetchTrips(userId: String): List<Trip>? {
        val tripsFilter = Filter.or(
            Filter.equalTo("tripCreatorId",userId),
            Filter.arrayContains("tripPalIds",userId)
        )
        return try {
            val trips = tripsCollectionRef.where(tripsFilter).get().await()
            val result = mutableListOf<Trip>()
            for (document in trips.documents){
                val trip = document.toObject(Trip::class.java)
                if (trip!=null){
                    trip.tripId = document.id
                    result.add(trip)
                }
            }
            result
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateTrip(tripId: String, updatedTrip: Trip){
        tripsCollectionRef.document(tripId).set(updatedTrip).await()
    }

    suspend fun fetchTrip(tripId: String): Trip?{
        return try{
            val tripReturned = tripsCollectionRef.document(tripId).get().await()
            val trip = tripReturned.toObject(Trip::class.java)
            trip

        } catch(e: Exception){
            null
        }
    }


    suspend fun createTrip(trip: Trip): String {
        val trip =  tripsCollectionRef.add(trip).await()
        return trip.id

    }
}