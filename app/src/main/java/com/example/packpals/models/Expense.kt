package com.example.packpals.models

data class Expense(
    val id: Int,
    val title: String,
    val date: String,
    val amountOwed: Double
)