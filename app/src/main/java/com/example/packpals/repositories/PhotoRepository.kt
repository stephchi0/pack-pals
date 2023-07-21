package com.example.packpals.repositories

import com.example.packpals.models.PhotoAlbum
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PhotoRepository @Inject constructor (private val photoCollectionRef: CollectionReference) {

    private val storage = FirebaseStorage.getInstance()

    suspend fun createAlbum(album: PhotoAlbum) {
        val newAlbum = photoCollectionRef.document().id
        album.albumId = newAlbum
        photoCollectionRef.document(newAlbum).set(album).await()
    }

    suspend fun addPhoto(album:  PhotoAlbum, photoImage: ByteArray) {

        val photoFileName =
            "photo_${System.currentTimeMillis()}.jpg"
        val photoStorageRef: StorageReference =
            storage.reference.child("photos").child(photoFileName)

        photoStorageRef.putBytes(photoImage).await()

        val photoURL = photoStorageRef.downloadUrl.await()

        val updatedPhotos = album.photos.toMutableList()
        updatedPhotos.add(photoURL.toString())
        album.photos = updatedPhotos

        photoCollectionRef.document(album.albumId).set(album).await()
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

    suspend fun fetchAlbum(albumId: String): PhotoAlbum? {
        val albumDocument = photoCollectionRef.document(albumId).get().await()

        return if (albumDocument.exists()) {
            val profileData = albumDocument.toObject(PhotoAlbum::class.java)
            profileData?.copy(albumId = albumDocument.id)
        } else {
            null
        }
    }
}