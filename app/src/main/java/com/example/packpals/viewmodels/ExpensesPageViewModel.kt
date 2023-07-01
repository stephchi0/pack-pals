package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Expense
import com.example.packpals.models.Pal
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.ExpensesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ExpensesPageViewModel @Inject constructor(private val authRepo: AuthRepository,
                                                private val expensesRepo: ExpensesRepository) : ViewModel() {
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
        val userId = authRepo.getCurrentUID()
        if (userId != null) {
            viewModelScope.launch {
                val result = expensesRepo.fetchExpenses(userId)
                if (result != null) {
                    _expensesList.value = result!!
                    createExpenseCardInfo()
                }
            }
        }
    }

    private fun createExpenseCardInfo() {
        val newExpenseCardInfoList = mutableListOf<Triple<String, String, String>>()
        for (expense in _expensesList.value!!) {
            val expenseCardTitle = expense.title!!
            val expenseCardDate = SimpleDateFormat("MM/dd/yyyy").format(expense.date)
            var expenseCardAmountMessage = ""
            if (expense.payerId == authRepo.getCurrentUID()) {
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
        val userId = authRepo.getCurrentUID()
        val debtorIdsSet = _payingPalIds.value
        if (userId != null && debtorIdsSet != null) {
            viewModelScope.launch {
                val newExpense = Expense(title, date, amountPaid, userId, debtorIdsSet.toList())
                expensesRepo.createExpense(newExpense)
            }
        }
    }
}