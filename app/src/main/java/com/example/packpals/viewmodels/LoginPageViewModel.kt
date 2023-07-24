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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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
    private val _toastMessage = MutableLiveData("")
    val toastMessage: LiveData<String> get() = _toastMessage
    private val _profilePictureUri: MutableLiveData<Uri> = MutableLiveData(Uri.EMPTY)
    val profilePictureUri: LiveData<Uri> get() = _profilePictureUri

    fun reset() {
        _loginSuccess.value = false
    }

    fun setProfilePictureUri(uri: Uri) {
        _profilePictureUri.value = uri
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val loginResult = authRepo.login(email, password)
            if (loginResult != null) {
                _loginSuccess.value = true
            }
            else {
                _toastMessage.value = "Invalid credentials. Please enter valid credentials or register a new account."
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                val registerResult = authRepo.register(email, password)
                val newUserId = registerResult.getOrNull()
                if (newUserId != null) {
                    var profilePictureURL: String? = null

                    val profilePictureUri = _profilePictureUri.value
                    if (profilePictureUri != null && profilePictureUri != Uri.EMPTY) {
                        profilePictureURL = storageRepo.uploadProfilePicture(newUserId, profilePictureUri).toString()
                    }

                    var newPal = Pal(newUserId, name, profilePictureURL, listOf(), listOf(), null, null, null)
                    palsRepo.createPal(newPal)

                    _registrationSuccess.value = true
                }
                else {
                    _toastMessage.value = when(registerResult.exceptionOrNull()) {
                        is FirebaseAuthWeakPasswordException -> "Weak password. Please enter a password that is at least 6 characters long."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email address. Please enter a valid email address."
                        is FirebaseAuthUserCollisionException -> "Email address already in use. Please enter a different email address."
                        else -> "Registration failed."
                    }
                }
            }
        }
        else {
            _toastMessage.value = "Please fill in all of the fields."
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = ""
    }
}