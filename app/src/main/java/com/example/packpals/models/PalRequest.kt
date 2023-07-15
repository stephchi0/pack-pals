package com.example.packpals.models

data class PalRequest(
    val id: String,
    val name: String,
    val profilePictureURL: String? = null,
)