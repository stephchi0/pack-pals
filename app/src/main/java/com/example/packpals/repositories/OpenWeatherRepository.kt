package com.example.packpals.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.URL
import javax.inject.Inject

class OpenWeatherRepository @Inject constructor(private val apiKey: String) {
    suspend fun fetchWeatherForLocation(latitude: Double, longitude: Double): String? =
        withContext(Dispatchers.IO) {

//            val apiKey = Resources.getSystem().getString(R.string.OPEN_WEATHER_API_KEY)
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
}