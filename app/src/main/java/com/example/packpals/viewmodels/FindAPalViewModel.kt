package com.example.packpals.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Pal
import com.example.packpals.models.PalRequest

class FindAPalViewModel : ViewModel() {
    val TAG = FindAPalViewModel::class.java.toString()

    private val _palRequestsLiveData = MutableLiveData(listOf(
        PalRequest("0", "Tuff"),
        PalRequest("1", "Feesh"),
        PalRequest("2", "Stooge"),
    ))

    val palRequestsLiveData: LiveData<List<PalRequest>>
        get() = _palRequestsLiveData

    fun addPal(username: String) {

    }
}