package com.example.packpals.views.pals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Pal
import com.example.packpals.models.PalRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FindAPalViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollectionRef = db.collection("pals")
    private val auth = FirebaseAuth.getInstance()

    val TAG = FindAPalViewModel::class.java.toString()

    private val _palRequestsLiveData = MutableLiveData(listOf(
        PalRequest("0", "Tuff"),
        PalRequest("1", "Feesh"),
        PalRequest("2", "Stooge"),
    ))

    val palRequestsLiveData: LiveData<List<PalRequest>>
        get() = _palRequestsLiveData
}