package com.example.packpals.repositories

import com.example.packpals.models.Pal
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PalsRepository @Inject constructor (private val palsCollectionRef: CollectionReference) {
    suspend fun fetchPal(userId: String): Pal? {
        return try{
            val palReturned = palsCollectionRef.document(userId).get().await()
            val pal = palReturned.toObject(Pal::class.java)
            if (pal != null) {
                pal.id = palReturned.id
            }
            pal

        } catch(e: Exception){
            null
        }
    }

    suspend fun createPal(id: String, name: String) {
        val palValues = mapOf("name" to name, "tripIds" to listOf<String>())
        palsCollectionRef.document(id).set(palValues).await()
    }
}