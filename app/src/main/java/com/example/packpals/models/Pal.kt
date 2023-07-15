package com.example.packpals.models

data class Pal(
    val id: String? = null,
    val name: String? = null,
    val profilePictureURL: String? = null,
    val tripIds: List<String>? = null,
    val gender: String? = null,
    val age: Int? = null,
    val bio: String? = null,
    val pals: Int? = null
)