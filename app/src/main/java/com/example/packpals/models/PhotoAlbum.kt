package com.example.packpals.models

data class PhotoAlbum (
    var albumName: String = "",
    var albumId: String = "",
    val tripId: String = "",
    var photos: List<String?> = listOf()
)