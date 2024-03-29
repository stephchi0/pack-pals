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
        try {
            val senderPal = fetchPal(senderId)
            val requestPal = fetchPal(palId)
            if (senderPal?.pals?.contains(requestPal?.id) == true) {
                return false
            }
            val palRequests = requestPal?.palRequests?.toMutableSet() ?: mutableListOf()
            palRequests.add(PalRequest(senderId, senderPal?.name, senderPal?.profilePictureURL))
            val newRequestPal = requestPal?.copy(palRequests = palRequests.toList())
            newRequestPal?.let {
                updatePal(it)
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun acceptPalRequest(id: String, newPalId: String): Boolean {
        try {
            val sendingPal = fetchPal(id)
            val receivingPal = fetchPal(newPalId)

            if (sendingPal?.pals == null || receivingPal?.pals == null
                || sendingPal.pals.contains(newPalId)
                || receivingPal.pals.contains(id) || receivingPal.palRequests == null) {
                return false
            }

            val newSendingPalsList = sendingPal.pals + newPalId
            val newReceivingPalsList = receivingPal.pals + id
            val newReceivingPalRequests = receivingPal.palRequests.toMutableList()
            newReceivingPalRequests.removeAll { request ->
                request.id == id
            }

            val newSendingPal = sendingPal.copy(pals = newSendingPalsList)
            val newReceivingPal = receivingPal.copy(pals = newReceivingPalsList, palRequests = newReceivingPalRequests)

            updatePal(newSendingPal)
            updatePal(newReceivingPal)

            return true
        } catch (e: Exception) {
            Log.w(TAG, "Error declining pal request for user $id from user $newPalId", e)
            return false
        }
    }

    suspend fun declinePalRequest(id: String, newPalId: String): Boolean {
        try {
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