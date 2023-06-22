package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Expense

class ExpensesPageViewModel : ViewModel() {
    val expensesList: LiveData<List<Expense>> = MutableLiveData(
        listOf(
            Expense(1, "dinner at bk?", "06/21/2023", 5.62),
            Expense(2, "test", "06/14/2023", 16.78)
        )
    )
}