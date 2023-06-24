package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPageViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollectionRef = db.collection("users")
    private val auth = FirebaseAuth.getInstance()
    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            println("Sign in successful")
            _loginSuccess.value = true
        }.addOnFailureListener {
            println("Sign in failed: ${it.message}")
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                val id = it.user?.uid
                if (id != null) {
                    val palValues = mapOf("name" to name, "tripIds" to listOf<String>())
                    usersCollectionRef.document(id).set(palValues)
                }
            }.addOnFailureListener {
                println("Registration failed: ${it.message}")
            }
        }
    }
}