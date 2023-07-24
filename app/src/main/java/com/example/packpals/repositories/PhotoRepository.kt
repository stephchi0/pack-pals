package com.example.packpals.repositories

import com.example.packpals.models.PhotoAlbum
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PhotoRepository @Inject constructor (private val photoCollectionRef: CollectionReference) {

    private val storage = FirebaseStorage.getInstance()

    suspend fun createAlbum(album: PhotoAlbum) {
        val newAlbum = photoCollectionRef.document().id
        album.albumId = newAlbum
        photoCollectionRef.document(newAlbum).set(album).await()
    }

    suspend fun addPhoto(albumId: String, photoURL: Uri) {
        val album = photoCollectionRef.document(albumId).get().await().toObject(PhotoAlbum::class.java)
        album?.let {
            val updatedPhotos = it.photos.toMutableList()
            updatedPhotos.add(photoURL.toString())
            it.photos = updatedPhotos
            photoCollectionRef.document(albumId).set(it).await()
        }
    }

    suspend fun deletePhoto(albumId: String, photoURL: String) {
        val albumDocument = photoCollectionRef.document(albumId).get().await()

        if (albumDocument.exists()) {
            val album = albumDocument.toObject(PhotoAlbum::class.java)
            album?.let {
                it.photos = it.photos.filterNot { photo -> photo == photoURL }
                photoCollectionRef.document(albumId).set(it).await()

                val photoRef = storage.getReferenceFromUrl(photoURL)
                photoRef.delete().await()
            }
        }
    }

    suspend fun fetchAlbums(tripId: String): List<PhotoAlbum> {
        val album = photoCollectionRef.whereEqualTo("tripId", tripId).get().await()

        val albumsList = mutableListOf<PhotoAlbum>()

        for (albumDocument in album) {
            val album = albumDocument.toObject(PhotoAlbum::class.java)
            val albumWithId = album.copy(albumId = albumDocument.id)
            albumsList.add(albumWithId)
        }
        return albumsList
    }
}