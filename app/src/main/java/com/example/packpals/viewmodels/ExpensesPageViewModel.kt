package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Expense
import com.example.packpals.models.Pal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class ExpensesPageViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val expensesCollectionRef = db.collection("expenses")
    private val auth = FirebaseAuth.getInstance()

    private val _expensesList: MutableLiveData<List<Expense>> = MutableLiveData(emptyList())
    private val _expenseCardInfoList: MutableLiveData<List<Triple<String, String, String>>> = MutableLiveData(emptyList())
    val expenseCardInfoList: LiveData<List<Triple<String, String, String>>> = _expenseCardInfoList // (title, date, amount)

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

    fun fetchExpenses() {
        val expensesFilter = Filter.or(
            Filter.equalTo("payerId", auth.currentUser?.uid),
            Filter.arrayContains("debtorIds", auth.currentUser?.uid)
        )

        expensesCollectionRef.where(expensesFilter).get().addOnSuccessListener { result ->
            val newExpenseList = mutableListOf<Expense>()
            for (expense in result.documents) {
                val expenseObject = expense.toObject(Expense::class.java)
                if (expenseObject != null) {
                    newExpenseList.add(expenseObject)
                }
            }
            _expensesList.value = newExpenseList
            createExpenseCardInfo()
        }
    }

    private fun createExpenseCardInfo() {
        val newExpenseCardInfoList = mutableListOf<Triple<String, String, String>>()
        for (expense in _expensesList.value!!) {
            val expenseCardTitle = expense.title!!
            val expenseCardDate = SimpleDateFormat("MM/dd/yyyy").format(expense.date)
            var expenseCardAmountMessage = ""
            if (expense.payerId == auth.currentUser?.uid) {
                val amount = expense.amountPaid!! * expense.debtorIds!!.size / (expense.debtorIds!!.size + 1)
                expenseCardAmountMessage = String.format("You are owed $%.2f", amount)
            }
            else {
                val amount = expense.amountPaid!! / (expense.debtorIds!!.size + 1)
                expenseCardAmountMessage = String.format("You owe $%.2f", amount)
            }
            newExpenseCardInfoList.add(Triple(expenseCardTitle, expenseCardDate, expenseCardAmountMessage))
        }
        _expenseCardInfoList.value = newExpenseCardInfoList
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
        val newExpense = Expense(title, date, amountPaid, auth.currentUser?.uid, _payingPalIds.value!!.toList())
        expensesCollectionRef.add(newExpense)
        fetchExpenses()
    }
}