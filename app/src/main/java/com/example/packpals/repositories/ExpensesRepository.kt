package com.example.packpals.repositories

import com.example.packpals.models.Expense
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExpensesRepository @Inject constructor(private val expensesCollectionRef: CollectionReference) {
    suspend fun fetchExpenses(userId: String, tripId: String): List<Expense>? {
        val expensesFilter = Filter.and(
            Filter.equalTo("tripId", tripId),
            Filter.or(
                Filter.equalTo("payerId", userId),
                Filter.arrayContains("debtorIds", userId)
            )
        )
        return try {
            val snapshot = expensesCollectionRef.where(expensesFilter).get().await()
            snapshot.toObjects(Expense::class.java).sortedByDescending { it.date }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createExpense(expense: Expense) {
        expensesCollectionRef.add(expense).await()
    }

    suspend fun updateExpense(expenseId: String,  updatedExpense: Expense) {
        expensesCollectionRef.document(expenseId).set(updatedExpense).await()
    }
}