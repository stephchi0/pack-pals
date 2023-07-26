package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Pal
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PalsFragmentViewModel @Inject constructor(private val palsRepo: PalsRepository,
                                                private val authRepo: AuthRepository) : ViewModel() {
    val TAG = PalsFragmentViewModel::class.java.toString()

    private val _palsLiveData = MutableLiveData(emptyList<Pal>())
    private var palsList = emptyList<Pal>()

    val palsLiveData: LiveData<List<Pal>>
        get() = _palsLiveData

    init {
        viewModelScope.launch {
            authRepo.getCurrentUID()?.let { id ->
                val userPal = palsRepo.fetchPal(id)
                val pals = userPal?.pals?.let {palsRepo.fetchPals(it)}
                pals?.let {
                    palsList = it
                    _palsLiveData.value = it
                }
            }
        }
    }

    fun filterPals(prefix: String) {
        _palsLiveData.value = palsList.filter { pal ->
            pal.name?.startsWith(prefix) == true
        }
    }
}