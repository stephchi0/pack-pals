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
import kotlin.math.round

@HiltViewModel
class ExpensesPageViewModel @Inject constructor(private val authRepo: AuthRepository,
                                                private val expensesRepo: ExpensesRepository) : ViewModel() {

    private val _expensesList: MutableLiveData<List<Expense>> = MutableLiveData(emptyList())

    private val _expenseCardInfoList: MutableLiveData<List<Triple<String, String, String>>> = MutableLiveData(emptyList())
    val expenseCardInfoList: LiveData<List<Triple<String, String, String>>> = _expenseCardInfoList // (title, date, amount)

    val palsList: LiveData<List<Pal>> = MutableLiveData(
        listOf(
            Pal("id1", "stooge"),
            Pal("id2", "mooge"),
            Pal("id3", "looge"),
            Pal("id4", "jooge")
        )
    )

    private val _totalCost: MutableLiveData<Double> = MutableLiveData(0.0)
    val totalCost: LiveData<Double> get() = _totalCost

    private val _debtorIdsSet: MutableLiveData<Set<String>> = MutableLiveData(emptySet())
    val debtorIdsSet: LiveData<Set<String>> get() = _debtorIdsSet

    private val _amountsOwedMap: MutableLiveData<Map<String, Double>> = MutableLiveData(emptyMap())
    val amountsOwedMap: LiveData<Map<String, Double>> get() = _amountsOwedMap

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
                val amount = expense.amountsOwed!!.values.sum()
                expenseCardAmountMessage = String.format("You are owed $%.2f", amount)
            }
            else {
                val userId = authRepo.getCurrentUID()
                val amount = expense.amountsOwed!![userId]
                expenseCardAmountMessage = String.format("You owe $%.2f", amount)
            }
            newExpenseCardInfoList.add(Triple(expenseCardTitle, expenseCardDate, expenseCardAmountMessage))
        }
        _expenseCardInfoList.value = newExpenseCardInfoList
    }

    fun setTotalCost(cost: Double) {
        val oldTotalCost = _totalCost.value
        _totalCost.value = round(cost * 100) / 100
        if (_totalCost.value != oldTotalCost) {
            updateAmountsOwedEvenly()
        }
    }

    fun addRemovePayingPal(palId: String) {
        if (_amountsOwedMap.value!!.containsKey(palId)) {
            _amountsOwedMap.value = _amountsOwedMap.value!!.minus(palId)
            updateAmountsOwedEvenly()
            _debtorIdsSet.value = _debtorIdsSet.value!!.minus(palId)
        }
        else {
            _amountsOwedMap.value = _amountsOwedMap.value!!.plus(Pair(palId, 0.0))
            updateAmountsOwedEvenly()
            _debtorIdsSet.value = _debtorIdsSet.value!!.plus(palId)
        }
    }

    private fun updateAmountsOwedEvenly() {
        val newAmountsOwed = _amountsOwedMap.value!!.toMutableMap()
        newAmountsOwed.replaceAll { _, _ -> round(_totalCost.value!!/(newAmountsOwed.size + 1) * 100) / 100 }
        _amountsOwedMap.value = newAmountsOwed
    }

    fun setAmountOwed(palId: String, amount: Double) {
        val newAmountsOwed = _amountsOwedMap.value!!.toMutableMap()
        newAmountsOwed[palId] = round(amount * 100) / 100
        _amountsOwedMap.value = newAmountsOwed
    }

    fun createExpense(title: String, date: Date) {
        viewModelScope.launch {
            val userId = authRepo.getCurrentUID()
            val amountPaid = _totalCost.value
            val debtorIds = _amountsOwedMap.value?.keys?.toList()
            val amountsOwed = _amountsOwedMap.value
            // TODO: add some validations, like sum of amountsOwed <= amountPaid

            val newExpense = Expense(title, date, amountPaid, userId, debtorIds, amountsOwed)
            expensesRepo.createExpense(newExpense)
        }
    }
}