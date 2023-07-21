package com.example.packpals.models

data class PhotoAlbum (
    var albumId: String,
    val title: String,
    val coverPhotoUrl: Int,
    var photos: List<String>
)