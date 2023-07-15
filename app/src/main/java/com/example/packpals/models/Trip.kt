package com.example.packpals.models

data class Trip(
    val title: String? = null,
    val tripCreatorId: String? = null,
    val tripPalIds: List<String>? = null,
    var active: Boolean? = null,
    var tripId: String? = null,
)