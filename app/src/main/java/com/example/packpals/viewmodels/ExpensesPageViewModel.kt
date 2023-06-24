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

    val palsList: MutableLiveData<List<Pal>> = MutableLiveData(
        listOf(
            Pal("id1", "stooge"),
            Pal("id2", "mooge"),
            Pal("id3", "looge"),
            Pal("id4", "jooge")
        )
    )

    private val _payingPalIds: MutableLiveData<Set<String>> = MutableLiveData(emptySet())
    val payingPalIds: LiveData<Set<String>> get() = _payingPalIds

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

    fun addRemovePayingPal(palId: String) {
        val newPayingPalIds = _payingPalIds.value!!.toMutableSet()
        if (palId in newPayingPalIds) {
            newPayingPalIds.remove(palId)
        }
        else {
            newPayingPalIds.add(palId)
        }

        _payingPalIds.value = newPayingPalIds
    }

    fun createExpense(title: String, date: Date, amountPaid: Double) {
        val newExpense = Expense(title, date, amountPaid, "payerid", _payingPalIds.value!!.toList())
        expensesCollectionRef.add(newExpense)
        fetchExpenses()
    }
}