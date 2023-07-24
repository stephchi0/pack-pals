package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.PalRequest
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomingPalRequestsViewModel @Inject constructor(
    private val palsRepo: PalsRepository,
    private val authRepo: AuthRepository
) : ViewModel() {
    val TAG = IncomingPalRequestsViewModel::class.java.toString()

    private val _palRequestsLiveData = MutableLiveData(emptyList<PalRequest>())

    init {
        viewModelScope.launch {
            val userPal = authRepo.getCurrentUID()?.let {
                palsRepo.fetchPal(it)
            }
            userPal?.palRequests?.let {
                _palRequestsLiveData.value = it
            }
        }
    }

    val palRequestsLiveData: LiveData<List<PalRequest>>
        get() = _palRequestsLiveData

    fun acceptPalRequest(userId: String) {
        viewModelScope.launch {
            authRepo.getCurrentUID()?.let { id ->
                if (palsRepo.acceptPalRequest(id, userId)) {
                    val newPalRequests = _palRequestsLiveData.value?.toMutableList()
                    newPalRequests?.removeAll { request ->
                        request.id == userId
                    }
                    _palRequestsLiveData.value = newPalRequests
                }
            }
        }
    }

    fun declinePalRequest(requestId: String) {
        viewModelScope.launch {
            authRepo.getCurrentUID()?.let { id ->
                if (palsRepo.declinePalRequest(id, requestId)) {
                    val newPalRequests = _palRequestsLiveData.value?.toMutableList()
                    newPalRequests?.removeAll { request ->
                        request.id == requestId
                    }
                    _palRequestsLiveData.value = newPalRequests
                }
            }
        }
    }
}