package com.example.packpals.repositories

import android.util.Log
import com.example.packpals.models.Pal
import com.example.packpals.models.PalRequest
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PalsRepository @Inject constructor (private val palsCollectionRef: CollectionReference) {

    companion object {
        val TAG = PalsRepository::class.java.toString()
    }

    suspend fun fetchPal(userId: String): Pal? {
        return try {
            val palReturned = palsCollectionRef.document(userId).get().await()
            palReturned.toObject(Pal::class.java)
        } catch(e: Exception) {
            null
        }
    }

    /**
     * queries for pals using userId/username/email
     */
    suspend fun queryPals(query: String): List<Pal> {
        return listOfNotNull(fetchPal(query)).ifEmpty {
            queryPalsByUsername(query).ifEmpty {
                queryPalsByEmail(query).ifEmpty { emptyList() }
            }
        }
    }

    suspend fun queryPalsByUsername(username: String): List<Pal> {
        return try {
            val queryResult = palsCollectionRef.whereEqualTo("name", username).get().await()
            queryResult.toObjects(Pal::class.java)
        } catch (e: Exception) {
            Log.w(TAG, "Error querying pal by username $username", e)
            emptyList()
        }
    }

    suspend fun queryPalsByEmail(email: String): List<Pal> {
        return try {
            val queryResult = palsCollectionRef.whereEqualTo("email", email).get().await()
            queryResult.toObjects(Pal::class.java)
        } catch (e: Exception) {
            Log.w(TAG, "Error querying pal by email ${email}", e)
            emptyList()
        }
    }

    suspend fun fetchPals(palIds: List<String>): List<Pal> {
        val tripPals = mutableListOf<Pal>()
        for (pal in palIds){
            val palItem = fetchPal(pal)
            if(palItem != null) {
                tripPals.add(palItem)
            }

        }
        return tripPals
    }

    suspend fun createPal(newPal: Pal) {
        val id = newPal.id
        if (!id.isNullOrEmpty()) {
            palsCollectionRef.document(id).set(newPal).await()
        }
    }

    suspend fun updatePal(pal: Pal) {
        pal.id?.let { id ->
            palsCollectionRef.document(id).set(pal).await()
        }
    }

    suspend fun editProfile(id: String, name: String, bio: String?, profilePictureURL: String?) {
        var palProfile = mapOf("name" to name, "bio" to bio)

        if (!profilePictureURL.isNullOrEmpty()) {
            palProfile = mapOf("name" to name, "bio" to bio, "profilePictureURL" to profilePictureURL)
        }

        palsCollectionRef.document(id).update(palProfile).await()
    }

    suspend fun fetchProfile(id: String): Pal? {
        val palEdit = palsCollectionRef.document(id).get().await()
        return if (palEdit.exists()) {
            palEdit.toObject(Pal::class.java)
        } else {
            null
        }
    }

    suspend fun sendPalRequest(senderId: String, palId: String): Boolean {
        return try {
            val senderPal = fetchPal(senderId)
            val requestPal = fetchPal(palId)
            val palRequests = senderPal?.palRequests?.toMutableSet() ?: mutableListOf()
            palRequests.add(PalRequest(palId, requestPal?.name, requestPal?.profilePictureURL))
            val newSenderPal = senderPal?.copy(palRequests = palRequests.toList())
            newSenderPal?.let {
                updatePal(it)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun acceptPalRequest(id: String, newPalId: String): Boolean {
        try {
            // TODO: check if newPalId is in requests
            // TODO: check if pal
            val pal = fetchPal(id)
            val newPalsList = if (pal?.pals?.contains(newPalId) == true) {
                pal.pals
            } else {
                buildList {
                    add(newPalId)
                    pal?.pals?.let {
                        addAll(it)
                    }
                }
            }


            val newPalRequests = pal?.palRequests?.toMutableList()
            newPalRequests?.removeAll { request ->
                request.id == newPalId
            }

            val newPal = pal?.copy(pals = newPalsList, palRequests = newPalRequests)
            newPal?.let { updatePal(it) }

            return true
        } catch (e: Exception) {
            Log.w(TAG, "Error declining pal request for user $id from user $newPalId", e)
            return false
        }
    }

    suspend fun declinePalRequest(id: String, newPalId: String): Boolean {
        try {
            // TODO: check if newPalId is in requests
            // TODO: check if pal
            val pal = fetchPal(id)

            val newPalRequests = pal?.palRequests?.toMutableList()
            newPalRequests?.removeAll { request ->
                request.id == newPalId
            }

            val newPal = pal?.copy(palRequests = newPalRequests)
            newPal?.let { updatePal(it) }

            return true
        } catch (e: Exception) {
            Log.w(TAG, "Error declining pal request for user $id from user $newPalId", e)
            return false
        }
    }
}