package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Pal
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.round

class ProfilePageViewModel @Inject constructor(
    private val profileRepo: ProfileRepository,
    private val authRepo: AuthRepository

    ): ViewModel() {
    private val _profile: MutableLiveData<Pal?> = MutableLiveData();
    val profile : LiveData<Pal?> get() = _profile;
    fun fetchProfile(userId: String) {
        viewModelScope.launch(){
            val userID = authRepo.getCurrentUID();
            val fetchProfile = profileRepo.fetchProfile(userId)
            _profile.value = fetchProfile
        }
    }

    fun updateProfile(profile: Pal) {
        viewModelScope.launch(){
            profileRepo.editProfile(profile)
        }
    }
    init {
        // profile data hardcoded in xml, move to here
    }
}