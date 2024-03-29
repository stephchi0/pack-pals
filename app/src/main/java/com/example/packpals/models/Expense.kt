package com.example.packpals.models

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Expense(
    @DocumentId
    var id: String? = null,
    val title: String? = null,
    val date: Date? = null,
    val tripId: String? = null,
    val amountPaid: Double? = null,
    val payerId: String? = null,
    val debtorIds: List<String>? = null,
    val amountsOwed: Map<String, Double>? = null, // debtor user id -> amount owed
    val settled: Boolean? = null
)