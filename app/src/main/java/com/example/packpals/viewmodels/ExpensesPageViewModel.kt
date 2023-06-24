package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Expense
import com.example.packpals.models.Pal
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class ExpensesPageViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val expensesCollectionRef = db.collection("expenses")

    private val _expensesList: MutableLiveData<List<Expense>> = MutableLiveData()
    val expensesList: LiveData<List<Expense>> get() = _expensesList

    init {
        fetchExpenses()
    }

    private fun fetchExpenses() {
        expensesCollectionRef.get().addOnSuccessListener { result ->
            val newExpenseList = mutableListOf<Expense>()
            for (expense in result.documents) {
                val expenseObject = expense.toObject(Expense::class.java)
                if (expenseObject != null) {
                    newExpenseList.add(expenseObject)
                }
            }
            _expensesList.value = newExpenseList
        }
    }
}