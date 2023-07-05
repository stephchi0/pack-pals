package com.example.packpals.repositories

import com.example.packpals.models.TripPhoto
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TripPhotosRepository @Inject constructor (private val tripPhotosCollectionRef: CollectionReference) {
    suspend fun fetchTripPhotos(tripId: String): List<TripPhoto>? {
        return try {
            tripPhotosCollectionRef.whereEqualTo("tripId", tripId).get().await().toObjects(TripPhoto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createTripPhoto(photo: TripPhoto) {
        tripPhotosCollectionRef.add(photo).await()
    }
}