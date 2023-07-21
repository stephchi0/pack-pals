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

class PlacesRepository @Inject constructor(){
    val key = "AIzaSyDYk8Xsk4AEFJ89f5M4I8ulMmOaSwLOUwA"

    // Returns the photo reference id from a given place name
    suspend fun photoIdFromName(name:String, maxWidth: Int = 200, maxLength: Int= 200): String? = 
        withContext(Dispatchers.IO){
            val url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json" +
                    "?fields=formatted_address%2Cname%2Cphotos%2Cgeometry" +
                    "&input=$name" +
                    "&inputtype=textquery" +
                    "&key=$key"

            return@withContext try {
                val response = URL(url).readText()
                val photoReference = parseCandidates(response)
                photoReference
            } catch (e: Exception) {
                null
            }
        }

    private fun parseCandidates(response: String): String {
        val json = Json.parseToJsonElement(response).jsonObject
        val candidates = json["candidates"]?.jsonArray?.get(0)
        val photoObject = candidates?.jsonObject?.get("photos")?.jsonArray?.get(0)
        return photoObject?.jsonObject?.get("photo_reference")?.jsonPrimitive?.contentOrNull ?: ""
    }

    fun photoUrlFromId(id: String): String{
        return "https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=200" +
                "&maxlength=200" +
                "&photo_reference=$id" +
                "&key=$key"
    }

}