package com.example.packpals.pages.expenses

data class Expense(
    val id: Int,
    val title: String,
    val date: String,
    val amountOwed: Double
)