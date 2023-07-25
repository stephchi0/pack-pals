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
import android.util.Log

@HiltViewModel
class ProfilePageViewModel @Inject constructor(
    private val profileRepo: PalsRepository,
    private val authRepo: AuthRepository

    ): ViewModel() {
    private val _profile: MutableLiveData<Pal?> = MutableLiveData();
    val profile : LiveData<Pal?> get() = _profile;

    fun fetchProfile() {
        viewModelScope.launch(){
            val id:String? = authRepo.getCurrentUID();
            id?.let { nonNullId ->
                val fetchProfile = profileRepo.fetchProfile(nonNullId)
                _profile.value = fetchProfile


            }

        }
    }
    fun updateProfile(name: String, gender: String?, bio: String?) {
        viewModelScope.launch(){
            val id:String? = authRepo.getCurrentUID();
            Log.d("ViewModel", "Editing profile for id: $id")
            Log.d("ViewModel","Received name: $name, gender: $gender")
            id?.let { nonNullId ->
                profileRepo.editProfile(nonNullId, name, gender, bio)
            }
        }
    }
    init {
    }
}