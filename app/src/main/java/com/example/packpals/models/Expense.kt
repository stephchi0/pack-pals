package com.example.packpals.models

import java.util.Date

data class Expense(
    val title: String? = null,
    val date: Date? = null,
    val amountPaid: Double? = null,
    val payerId: String? = null,
    val debtorIds: List<String>? = null,
    val amountsOwed: Map<String, Double>? = null, // debtor user id -> amount owed
    val settled: Boolean? = null,
    var expenseId: String? = null
)