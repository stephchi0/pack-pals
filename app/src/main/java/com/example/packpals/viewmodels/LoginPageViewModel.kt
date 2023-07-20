package com.example.packpals.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Pal
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.PalsRepository
import com.example.packpals.repositories.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(private val authRepo: AuthRepository,
                                             private val palsRepo: PalsRepository,
                                             private val storageRepo: StorageRepository) : ViewModel() {
    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess
    private val _registrationSuccess = MutableLiveData(false)
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess
    private var _profilePictureUri: Uri = Uri.EMPTY

    fun reset() {
        _loginSuccess.value = false
        _registrationSuccess.value = false
        _profilePictureUri = Uri.EMPTY
    }

    fun setProfilePictureUri(uri: Uri) {
        _profilePictureUri = uri
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
                    var profilePictureURL: String? = null
                    if (_profilePictureUri != Uri.EMPTY) {
                        profilePictureURL = storageRepo.uploadProfilePicture(newUserId, _profilePictureUri).toString()
                    }

                    var newPal = Pal(newUserId, name, profilePictureURL, listOf(), listOf(), null, null, null)
                    palsRepo.createPal(newPal)

                    _registrationSuccess.value = true
                }
            }
        }
    }
}