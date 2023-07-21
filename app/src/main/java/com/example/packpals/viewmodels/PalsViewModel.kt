package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Pal
import com.example.packpals.models.PalRequest
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PalsRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PalsViewModel @Inject constructor(private val palsRepo: PalsRepository,
                                        private val authRepo: AuthRepository) : ViewModel() {

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

    private val _palRequestQueryResultLiveData = MutableLiveData(listOf(
        Pal(id = "0", name = "mooge"),
        Pal(id = "1", name = "feesh"),
    ))
    val palRequestQueryResultLiveData: LiveData<List<Pal>>
        get() = _palRequestQueryResultLiveData

    fun addPal(userId: String) {
        viewModelScope.launch {
            authRepo.getCurrentUID()?.run {
                palsRepo.addPal(this, userId)
            }
        }
    }
}