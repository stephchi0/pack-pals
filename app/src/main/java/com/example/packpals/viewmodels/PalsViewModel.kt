package com.example.packpals.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Pal
import com.example.packpals.models.PalRequest
import com.google.firebase.firestore.FirebaseFirestore

class PalsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollectionRef = db.collection("pals")

    val TAG = PalsViewModel::class.java.toString()

    private val _palRequestsLiveData = MutableLiveData(listOf(
        PalRequest("0", "Tuff"),
        PalRequest("1", "Feesh"),
        PalRequest("2", "Stooge"),
    ))

    val palRequestsLiveData: LiveData<List<PalRequest>>
        get() = _palRequestsLiveData

    fun addPal(query: String) {
        usersCollectionRef.whereEqualTo("name", query)
            .get()
            .addOnSuccessListener { documents ->
                val palObjects = documents.map { it.toObject(Pal::class.java) }
                Log.d(TAG, "${palObjects[0]}")
            }.addOnFailureListener { exception ->
                Log.w(TAG, "addPal error", exception)
            }
    }
}