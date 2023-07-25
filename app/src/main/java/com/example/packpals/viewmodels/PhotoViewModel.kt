package com.example.packpals.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.PhotoAlbum
import com.example.packpals.repositories.PhotoRepository
import com.example.packpals.repositories.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.packpals.repositories.StorageRepository

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val albumRepo: PhotoRepository,
    private val photoRepo: StorageRepository,
    private val tripRepo: TripsRepository

): ViewModel() {
    private val _albums: MutableLiveData<List<PhotoAlbum?>> = MutableLiveData();
    val albums : LiveData<List<PhotoAlbum?>> get() = _albums;

    fun fetchAlbums() {
        val tripId = tripRepo.selectedTrip.tripId
        viewModelScope.launch(){
            tripId?.let { nonNullId ->
                val fetchAlbum = albumRepo.fetchAlbums(nonNullId)
                _albums.value = fetchAlbum
            }
        }
    }
    fun addPhoto(albumId: String, imageUri: Uri, context: Context) {
        viewModelScope.launch(){
            albumId?.let { nonNullId ->
                albumRepo.addPhoto(nonNullId, photoRepo.addPhotoStorage(imageUri, context))
                Log.d("viewmodel", albumId)

                fetchAlbums()
            }
        }
    }
    fun createAlbum(albumName: String) {
        val tripId = tripRepo.selectedTrip.tripId
        val album = tripId?.let { PhotoAlbum (albumName, "", it, emptyList()) }
        Log.d("view model button press", albumName)
        viewModelScope.launch(){
            if (album != null) {
                albumRepo.createAlbum(album)

                fetchAlbums()
            }
        }
    }
}