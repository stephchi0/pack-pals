package com.example.packpals.models

import java.util.Date

data class Trip(
    val title: String? = null,
    val tripCreatorId: String? = null,
    val tripPalIds: List<String>? = null,
    val status: String? = "upcoming", //default to upcoming
)