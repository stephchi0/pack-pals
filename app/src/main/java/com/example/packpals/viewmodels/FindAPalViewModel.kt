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
class FindAPalViewModel @Inject constructor(
    private val palsRepo: PalsRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _palRequestQueryResultLiveData = MutableLiveData(emptyList<Pal>())
    val palRequestQueryResultLiveData: LiveData<List<Pal>>
        get() = _palRequestQueryResultLiveData

    fun queryPals(query: String) {
        viewModelScope.launch {
            val queryResult = palsRepo.queryPals(query)
            _palRequestQueryResultLiveData.value = queryResult.filterNot { pal ->
                pal.id == authRepo.getCurrentUID()
            }
        }
    }

    fun sendPalRequest(userId: String, onCompleteListener: (success: Boolean) -> Unit) {
        viewModelScope.launch {
            authRepo.getCurrentUID()?.let {
                onCompleteListener(palsRepo.sendPalRequest(it, userId))
            }
        }
    }
}