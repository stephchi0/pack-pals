package com.example.packpals.models

import com.google.firebase.firestore.DocumentId

data class Pal(
    @DocumentId
    var id: String? = null,
    val name: String? = null,
    val profilePictureURL: String? = null,
    val pals: List<String>? = null, // list of pal ids
    val palRequests: List<PalRequest>? = null, // list of incoming pal requests
    val bio: String? = null,
)