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
class ProfilePageViewModel @Inject constructor(
    private val profileRepo: PalsRepository,
    private val authRepo: AuthRepository,
    private val storageRepo: StorageRepository
    ): ViewModel() {
    private val _profile: MutableLiveData<Pal> = MutableLiveData(Pal())
    val profile : LiveData<Pal> get() = _profile
    private val _profilePictureUri: MutableLiveData<Uri> = MutableLiveData(Uri.EMPTY)
    val profilePictureUri: LiveData<Uri> get() = _profilePictureUri

    private fun fetchProfile() {
        viewModelScope.launch(){
            val id:String? = authRepo.getCurrentUID();
            id?.let { nonNullId ->
                val fetchProfile = profileRepo.fetchProfile(nonNullId)
                _profile.value = fetchProfile ?: Pal()
            }
        }
    }
    fun updateProfile(name: String, bio: String?) {
        viewModelScope.launch(){
            val id:String? = authRepo.getCurrentUID();
            id?.let { nonNullId ->
                var profilePictureURL: String? = null

                val fileUri = _profilePictureUri.value
                if (fileUri != null && fileUri != Uri.EMPTY) {
                    profilePictureURL = storageRepo.uploadProfilePicture(nonNullId, fileUri).toString()
                }

                profileRepo.editProfile(nonNullId, name, bio, profilePictureURL)
            }

            fetchProfile()
        }
    }

    fun setProfilePictureUri(uri: Uri) {
        _profilePictureUri.value = uri
    }

    init {
        fetchProfile()
    }
}