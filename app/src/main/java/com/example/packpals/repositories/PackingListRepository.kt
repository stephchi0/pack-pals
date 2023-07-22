package com.example.packpals.repositories

import com.example.packpals.models.Expense
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class PackingListRepository @Inject constructor(private val packingListCollectionRef: CollectionReference) {

    suspend fun createExpense(expense: Expense) {

    }
    suspend fun updateExpense(expenseId: String,  updatedExpense: Expense) {
    }
}