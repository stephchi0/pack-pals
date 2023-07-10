package com.example.packpals.viewmodels

import android.view.View
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


    private val _createExpenseSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val createExpenseSuccess: LiveData<Boolean> get() = _createExpenseSuccess

    private val _totalCost: MutableLiveData<Double> = MutableLiveData(0.0)

    enum class ExpenseSplittingMethod {
        EQUAL, PERCENTAGE, EXACT
    }
    private val _splitMethod: MutableLiveData<ExpenseSplittingMethod> = MutableLiveData(ExpenseSplittingMethod.EQUAL)
    val splitMethod: LiveData<ExpenseSplittingMethod> get() = _splitMethod

    val palsList: LiveData<List<Pal>> = MutableLiveData( // TODO: retrieve trip pals when trip functionality is finished
        listOf(
            Pal("id1", "stooge"),
            Pal("id2", "mooge"),
            Pal("id3", "looge"),
            Pal("id4", "jooge")
        )
    )

    private val _debtorIdsSet: MutableLiveData<Set<String>> = MutableLiveData(emptySet())
    val debtorIdsSet: LiveData<Set<String>> get() = _debtorIdsSet

    private val _amountsOwedMap: MutableLiveData<Map<String, Double>> = MutableLiveData(emptyMap())
    val amountsOwedMap: LiveData<Map<String, Double>> get() = _amountsOwedMap

    private val _toastMessage: MutableLiveData<String> = MutableLiveData("")
    val toastMessage: LiveData<String> get() = _toastMessage

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
                expenseCardAmountMessage = String.format("You are owed: $%.2f", amount)
            }
            else {
                val userId = authRepo.getCurrentUID()
                val amount = expense.amountsOwed!![userId]
                expenseCardAmountMessage = String.format("You owe: $%.2f", amount)
            }
            newExpenseCardInfoList.add(Triple(expenseCardTitle, expenseCardDate, expenseCardAmountMessage))
        }
        _expenseCardInfoList.value = newExpenseCardInfoList
    }

    fun setTotalCost(cost: Double) {
        _totalCost.value = round(cost * 100) / 100
    }

    fun setSplitMethod(method: ExpenseSplittingMethod) {
        val newAmountsOwedMap = _amountsOwedMap.value?.toMutableMap()
        newAmountsOwedMap?.replaceAll { _, _ -> 0.0 }

        _splitMethod.value = method
    }

    fun addRemovePayingPal(palId: String) {
        if (_amountsOwedMap.value!!.containsKey(palId)) {
            _amountsOwedMap.value = _amountsOwedMap.value!!.minus(palId)
            _debtorIdsSet.value = _debtorIdsSet.value!!.minus(palId)
        }
        else {
            _amountsOwedMap.value = _amountsOwedMap.value!!.plus(Pair(palId, 0.0))
            _debtorIdsSet.value = _debtorIdsSet.value!!.plus(palId)
        }
    }

    fun setAmountOwed(palId: String, amount: Double) {
        val newAmountsOwed = _amountsOwedMap.value!!.toMutableMap()
        if (_splitMethod.value == ExpenseSplittingMethod.EXACT) {
            newAmountsOwed[palId] = round(amount * 100) / 100
        }
        else {
            newAmountsOwed[palId] = amount
        }
        _amountsOwedMap.value = newAmountsOwed
    }

    fun createExpense(title: String, date: Date) {
        viewModelScope.launch {
            val newExpense = Expense(
                title,
                date,
                _totalCost.value,
                authRepo.getCurrentUID(),
                _debtorIdsSet.value?.toList(),
                splitExpense(_totalCost.value!!, _amountsOwedMap.value!!.toMutableMap(), _splitMethod.value!!)
            )

            if (validateExpense(newExpense)) {
                expensesRepo.createExpense(newExpense)
                _toastMessage.value = "Successfully created expense"
                _createExpenseSuccess.value = true
            }
        }
    }

    private fun splitExpense(totalCost: Double, amounts: Map<String, Double>, method: ExpenseSplittingMethod): Map<String, Double> {
        val newAmounts = amounts.toMutableMap()
        when (method) {
            ExpenseSplittingMethod.EQUAL -> {
                newAmounts.replaceAll { _, _ -> round(totalCost/(amounts.size + 1) * 100) / 100 }
            }
            ExpenseSplittingMethod.PERCENTAGE -> {
                newAmounts.replaceAll { _, percentage -> round(percentage * totalCost) / 100 }
            }
            ExpenseSplittingMethod.EXACT -> {
                newAmounts.replaceAll { _, value -> round(value * 100) / 100 }
            }
        }
        return newAmounts
    }

    private fun validateExpense(expense: Expense): Boolean {
        if (expense.title.isNullOrEmpty()) {
            _toastMessage.value = "Cannot have empty expense name"
            return false
        }
        if (expense.date == null) return false
        if (expense.amountPaid == null || expense.amountPaid <= 0) {
            _toastMessage.value = "Cannot have no expense total"
            return false
        }
        if (expense.payerId.isNullOrEmpty()) return false
        if (expense.debtorIds == null) return false
        if (expense.amountsOwed == null) return false

        if (expense.amountsOwed.size != expense.debtorIds.size || expense.amountsOwed.values.sum() > expense.amountPaid) {
            _toastMessage.value = "Total of amounts owed cannot exceed expense total"
            return false
        }

        return true
    }

    fun clearToastMessage() {
        _toastMessage.value = ""
    }
}