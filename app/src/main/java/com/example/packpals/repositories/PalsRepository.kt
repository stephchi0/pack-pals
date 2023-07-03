package com.example.packpals.repositories

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PalsRepository @Inject constructor (private val palsCollectionRef: CollectionReference) {
    suspend fun createPal(id: String, name: String) {
        val palValues = mapOf("name" to name, "tripIds" to listOf<String>())
        palsCollectionRef.document(id).set(palValues).await()
    }
}