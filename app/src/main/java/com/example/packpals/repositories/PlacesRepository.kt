package com.example.packpals.repositories
import com.google.firebase.firestore.GeoPoint
import com.example.packpals.models.Location
import com.example.packpals.models.SearchResultItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.URL
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val openWeatherRepo: OpenWeatherRepository
    ){
//    val key = Resources.getSystem().getString(R.string.MAPS_API_KEY)
    //Todo: dont hardcode
    val key ="AIzaSyDYk8Xsk4AEFJ89f5M4I8ulMmOaSwLOUwA"

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
                val photoReference = parsePhotoCandidates(response)
                photoReference
            } catch (e: Exception) {
                null
            }
        }

    private fun parsePhotoCandidates(response: String): String {
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

    suspend fun autocompleteResults(search: String): List<SearchResultItem>? =
        withContext(Dispatchers.IO){
        val url = "https://maps.googleapis.com/maps/api/place/autocomplete/json" +
                "?input=$search" +
                "&key=$key"

        return@withContext try {
            val response = URL(url).readText()
            val predictions = parseSearchCandidates(response)
            predictions
        } catch (e: Exception) {
            null
        }
    }

    private fun parseSearchCandidates(response: String): List<SearchResultItem> {
        val json = Json.parseToJsonElement(response).jsonObject
        val predictions = json["predictions"]?.jsonArray

        val itemList = mutableListOf<SearchResultItem>()

        predictions?.forEach { candidateElement ->
            val searchResult = SearchResultItem()
            val mainText = candidateElement.jsonObject["structured_formatting"]
                ?.jsonObject?.get("main_text")?.jsonPrimitive?.content
            if (mainText != null) {
                searchResult.addMainText(mainText)
            }

            val secondaryText = candidateElement.jsonObject["structured_formatting"]
                ?.jsonObject?.get("secondary_text")?.jsonPrimitive?.content
            if (secondaryText != null) {
                searchResult.addSecondaryText(secondaryText)
            }
            itemList.add(searchResult)
        }
        return itemList
    }

    suspend fun locationDetailsFromString(name:String, maxWidth: Int = 200, maxLength: Int= 200): GeoPoint? =
        withContext(Dispatchers.IO){
            val url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json" +
                    "?fields=formatted_address%2Cname%2Cgeometry" +
                    "&input=$name" +
                    "&inputtype=textquery" +
                    "&key=$key"
            return@withContext try {
                val response = URL(url).readText()
                val coordinates = parseDetails(response)
                var geo = GeoPoint(0.0,0.0)
                if(coordinates.longitude != null && coordinates.latitude != null){
                    geo = GeoPoint(coordinates.latitude, coordinates.longitude)
                }
                geo
            } catch (e: Exception) {
                null
            }
        }

    private fun parseDetails(response: String): Location {
        val json = Json.parseToJsonElement(response).jsonObject
        val candidates = json["candidates"]?.jsonArray?.get(0)
        val geometryObject = candidates?.jsonObject?.get("geometry")?.jsonObject

        val latitude = geometryObject?.get("location")?.jsonObject?.get("lat")?.jsonPrimitive?.contentOrNull ?: ""
        val longitude = geometryObject?.get("location")?.jsonObject?.get("lng")?.jsonPrimitive?.contentOrNull ?: ""

        return Location(latitude.trim().replace("\uFEFF", "").toDouble(),longitude.trim().replace("\uFEFF", "").toDouble())
    }
}