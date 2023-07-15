package com.example.packpals.repositories

import com.example.packpals.models.Expense
import com.example.packpals.models.Itinerary_Item
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ItineraryRepository @Inject constructor(private val itineraryCollectionRef: CollectionReference) {
    suspend fun fetchItems(tripId: String): List<Itinerary_Item>? {
        val tripFilter = Filter.equalTo("tripId", tripId)
        return try {
            itineraryCollectionRef.where(tripFilter).get().await().toObjects(Itinerary_Item::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createItem(item: Itinerary_Item) {
        itineraryCollectionRef.add(item).await()
    }
}