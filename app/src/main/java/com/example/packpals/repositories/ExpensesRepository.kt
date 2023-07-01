package com.example.packpals.repositories

import com.example.packpals.models.Expense
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class ExpensesRepository @Inject constructor(private val expensesCollectionRef: CollectionReference) {
    suspend fun fetchExpenses(userId: String): List<Expense>? {
        val expensesFilter = Filter.or(
            Filter.equalTo("payerId", userId),
            Filter.arrayContains("debtorIds", userId)
        )
        return try {
            expensesCollectionRef.where(expensesFilter).get().await().toObjects(Expense::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun createExpense(expense: Expense) {
        expensesCollectionRef.add(expense)
    }
}