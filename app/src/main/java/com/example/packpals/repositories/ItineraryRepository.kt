package com.example.packpals.repositories

import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import com.example.packpals.R
import com.example.packpals.models.Expense
import com.example.packpals.models.Itinerary_Item
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.io.FileInputStream
import java.net.URL
import java.util.Properties
import javax.inject.Inject


class ItineraryRepository @Inject constructor(
    private val itineraryCollectionRef: CollectionReference,
    private val openWeatherRepo: OpenWeatherRepository
    ) {


    suspend fun fetchItems(tripId: String): List<Itinerary_Item>? {
        val tripFilter = Filter.equalTo("tripId", tripId)
        var itineraryItems = emptyList<Itinerary_Item>()
        val newItineraryItems = mutableListOf<Itinerary_Item>()
        val newItineraryItemsWithId = mutableListOf<Itinerary_Item>()

        itineraryItems = itineraryCollectionRef.where(tripFilter).get().await().toObjects(Itinerary_Item::class.java)

        val snapshot = itineraryCollectionRef.where(tripFilter).get().await()
        for (document in snapshot.documents) {
            val item = document.toObject(Itinerary_Item::class.java)
            if (item != null) {
                item.itemId = document.id
                newItineraryItems.add(item)
            }
        }

        newItineraryItems.forEach { item ->
            val weather = openWeatherRepo.fetchWeatherForLocation(item.geopoint?.latitude ?: 0.0, item.geopoint?.longitude ?: 0.0)
            if (weather != null) {
                val updatedItem = item.copy()
                updatedItem.addForecast(weather)
                newItineraryItemsWithId.add(updatedItem)
            }
        }

        return newItineraryItemsWithId
    }

    suspend fun createItem(item: Itinerary_Item) {
        val itineraryItem = object{
            val location = item.location
            val startDate = item.startDate
            val endDate = item.endDate
            val geopoint = item.geopoint
            val tripId = item.tripId
        }
        itineraryCollectionRef.add(itineraryItem).await()
    }

    suspend fun updateItem(item:Itinerary_Item) {
        val itineraryItem = object{
            val location = item.location
            val startDate = item.startDate
            val endDate = item.endDate
            val geopoint = item.geopoint
            val tripId = item.tripId
        }
        itineraryCollectionRef.document(item.itemId!!).set(itineraryItem).await()
    }
}