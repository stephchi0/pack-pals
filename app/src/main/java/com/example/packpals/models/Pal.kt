package com.example.packpals.models

data class Pal(
    val id: String? = null,
    val name: String? = null,
    val profilePictureURL: String? = null,
    val tripIds: List<String>? = null,
    val pals: List<String>? = null, // list of pal ids
    val palRequests: List<PalRequest>? = null, // list of incoming pal requests
)