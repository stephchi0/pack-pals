package com.example.packpals.repositories

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class StorageRepository @Inject constructor (private val storage: FirebaseStorage) {
    suspend fun uploadProfilePicture(userId: String, fileUri: Uri): Uri? {
        return uploadFile("images/profile_pictures/$userId.jpg", fileUri)
    }

    suspend fun uploadTripPhoto(tripId: String, fileUri: Uri): Uri? {
        return uploadFile("images/trip_photos/$tripId/${UUID.randomUUID()}.jpg", fileUri)
    }

    private suspend fun uploadFile(path: String, fileUri: Uri): Uri? {
        val ref = storage.reference.child(path)
        return try {
            val result = ref.putFile(fileUri).await()
            result.metadata?.reference?.downloadUrl?.await()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addPhotoStorage(albumId: String, photoImage: ByteArray): Uri {

        val photoFileName =
            "photo_${System.currentTimeMillis()}.jpg"
        val photoStorageRef: StorageReference =
            storage.reference.child("photos").child(photoFileName)

        photoStorageRef.putBytes(photoImage).await()

        return photoStorageRef.downloadUrl.await()
    }
}