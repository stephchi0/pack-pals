package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(private val authRepo: AuthRepository,
                                             private val palsRepo: PalsRepository) : ViewModel() {
    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess
    private val _registrationSuccess = MutableLiveData(false)
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    fun reset() {
        _loginSuccess.value = false
        _registrationSuccess.value = false
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepo.login(email, password)
            _loginSuccess.value = true
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                val newUserId = authRepo.register(email, password)
                if (newUserId != null) {
                    _registrationSuccess.value = true
                    palsRepo.createPal(newUserId, name)
                }
            }
        }
    }
}