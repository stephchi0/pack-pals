package com.example.packpals.models

data class Trip(
    val title: String? = null,
    val tripCreatorId: String? = null,
    val tripPalIds: List<String>? = null,
    val active: Boolean? = true //default to active after creation
)