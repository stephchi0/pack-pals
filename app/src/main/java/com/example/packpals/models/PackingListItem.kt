package com.example.packpals.models


data class PackingListItem(
    var itemId: String? = null,
    val title: String? = null,
    val tripId: String? = null,
    val ownerId: String? = null,
    var packed: Boolean? = null,
    var group: Boolean? = null,
)