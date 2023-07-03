package com.example.packpals.repositories

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor (private val auth: FirebaseAuth) {
    fun getCurrentUID(): String? {
        return auth.currentUser?.uid
    }

    suspend fun login(email: String, password: String): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            return result.user?.uid
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(email: String, password: String): String? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            return result.user?.uid
        } catch (e: Exception) {
            null
        }
    }
}