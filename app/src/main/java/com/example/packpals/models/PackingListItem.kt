package com.example.packpals.models


data class PackingListItem(
    val title: String? = null,
    val tripId: String? = null,
    var packed: Boolean? = null,
    var group: Boolean? = null,
)