package com.example.packpals.repositories

import com.example.packpals.models.Pal
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val PalsCollectionRef: CollectionReference) {
    suspend fun fetchProfile(userId: String): Pal? {
        val querySnapshot = PalsCollectionRef.whereEqualTo("id", userId).limit(1).get().await();
        return querySnapshot.documents.firstOrNull()?.toObject(Pal::class.java)
    }

    suspend fun editProfile(profile: Pal) {
        val documentRef = PalsCollectionRef.document(profile.id!!)
        documentRef.set(profile).await()
    }
}