package com.example.packpals.repositories

import android.content.pm.PackageManager
import android.os.Bundle
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


class ItineraryRepository @Inject constructor(private val itineraryCollectionRef: CollectionReference) {

    suspend fun fetchItems(tripId: String): List<Itinerary_Item>? {
        val tripFilter = Filter.equalTo("tripId", tripId)
        var itineraryItems = emptyList<Itinerary_Item>()

        try {
            itineraryItems = itineraryCollectionRef.where(tripFilter).get().await().toObjects(Itinerary_Item::class.java)
        } catch (e: Exception) {
            return null
        }

        val newItineraryItems = mutableListOf<Itinerary_Item>()
        itineraryItems.forEach { item ->
            val weather = fetchWeatherForLocation(item.geopoint?.latitude ?: 0.0, item.geopoint?.longitude ?: 0.0)
            if (weather != null) {
                val updatedItem = item.copy()
                updatedItem.addForecast(weather)
                newItineraryItems.add(updatedItem)
            }
        }

        return newItineraryItems
    }

    private suspend fun fetchWeatherForLocation(latitude: Double, longitude: Double): String? =
        withContext(Dispatchers.IO) {
            val properties = Properties()
            val apiKey = "44988b69e712d97ad5d74b585530ddf4" // TODO: grab from localproperties idk how yet
            val url = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey"

            return@withContext try {
                val response = URL(url).readText()
                val ret = parseDescription(response)
                ret
            } catch (e: Exception) {
                null
            }
        }

    private fun parseDescription(response: String): String {
        val json = Json.parseToJsonElement(response).jsonObject
        val weather = json["weather"]?.jsonArray?.get(0)
        return weather?.jsonObject?.get("description")?.jsonPrimitive?.contentOrNull ?: ""
    }

    suspend fun createItem(item: Itinerary_Item) {
        itineraryCollectionRef.add(item).await()
    }
}