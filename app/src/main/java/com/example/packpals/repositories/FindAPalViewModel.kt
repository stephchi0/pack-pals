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
class FindAPalViewModel @Inject constructor(private val palsRepo: PalsRepository,
                                                private val authRepo: AuthRepository) : ViewModel() {
    val TAG = FindAPalViewModel::class.java.toString()

    private val _palRequestQueryResultLiveData = MutableLiveData(emptyList<Pal>())
    val palRequestQueryResultLiveData: LiveData<List<Pal>>
        get() = _palRequestQueryResultLiveData

    fun queryPals(query: String) {
        viewModelScope.launch {
            val queryResult = palsRepo.queryPals(query)
            _palRequestQueryResultLiveData.value = queryResult
        }
    }

    fun sendPalRequest(userId: String) {
        viewModelScope.launch {
            // call palsrepo method here
        }
    }
}