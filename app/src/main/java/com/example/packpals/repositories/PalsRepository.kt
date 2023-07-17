package com.example.packpals.repositories

import com.example.packpals.models.Pal
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import android.util.Log

class PalsRepository @Inject constructor (private val palsCollectionRef: CollectionReference) {
    suspend fun createPal(id: String, name: String) {
        val palValues = mapOf("name" to name, "tripIds" to listOf<String>())
        palsCollectionRef.document(id).set(palValues).await()
    }

    suspend fun editProfile(id: String, name: String, gender: String?, bio: String?) {
        val palProfile = mapOf("name" to name, "gender" to gender, "bio" to bio)
        palsCollectionRef.document(id).set(palProfile).await()
    }
    suspend fun fetchProfile(id: String): Pal? {
        val palEdit = palsCollectionRef.document(id).get().await()
        return if (palEdit.exists()) {
            val profileData = palEdit.toObject(Pal::class.java)
            profileData?.copy(id = palEdit.id)
        } else {
            null
        }
    }
}