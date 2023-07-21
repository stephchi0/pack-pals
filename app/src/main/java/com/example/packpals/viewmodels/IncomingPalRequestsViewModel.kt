package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Pal
import com.example.packpals.models.PalRequest
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomingPalRequestsViewModel @Inject constructor(private val palsRepo: PalsRepository,
                                                private val authRepo: AuthRepository) : ViewModel() {
    val TAG = IncomingPalRequestsViewModel::class.java.toString()

    private val _palRequestsLiveData = MutableLiveData(listOf(
        PalRequest("0", "Tuff"),
        PalRequest("1", "Feesh"),
        PalRequest("2", "Stooge"),
    ))

    val palRequestsLiveData: LiveData<List<PalRequest>>
        get() = _palRequestsLiveData

    fun acceptPalRequest(userId: String) {
        viewModelScope.launch {
            authRepo.getCurrentUID()?.run {
                palsRepo.acceptPalRequest(this, userId)
            }
        }
    }
}