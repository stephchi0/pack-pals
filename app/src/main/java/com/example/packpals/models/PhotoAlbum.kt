package com.example.packpals.models

data class PhotoAlbum (
    var albumId: String,
    val tripId: String,
    var photos: List<String?>
)