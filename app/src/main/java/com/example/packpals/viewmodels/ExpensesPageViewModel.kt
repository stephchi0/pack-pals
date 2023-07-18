package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packpals.models.Expense
import com.example.packpals.models.Pal
import com.example.packpals.repositories.AuthRepository
import com.example.packpals.repositories.ExpensesRepository
import com.example.packpals.repositories.PalsRepository
import com.example.packpals.repositories.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class ExpensesPageViewModel @Inject constructor(private val authRepo: AuthRepository,
                                                private val expensesRepo: ExpensesRepository,
                                                private val tripsRepo: TripsRepository,
                                                private val palsRepo: PalsRepository) : ViewModel() {

    private val _expensesList: MutableLiveData<List<Expense>> = MutableLiveData(emptyList())
    val expensesList: LiveData<List<Expense>> get() = _expensesList


    // TODO: use better way to handle events instead of boolean livedata
    private val _updateNewExpenseInputs: MutableLiveData<Boolean> = MutableLiveData(false)
    val updateNewExpenseInputs: LiveData<Boolean> get() = _updateNewExpenseInputs

    private val _createExpenseSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val createExpenseSuccess: LiveData<Boolean> get() = _createExpenseSuccess

    private val _toastMessage: MutableLiveData<String> = MutableLiveData("")
    val toastMessage: LiveData<String> get() = _toastMessage


    enum class ExpenseSplittingMethod {
        EQUAL, PERCENTAGE, EXACT
    }
    private var _editExpenseId: String = ""
    private val _expenseTitle: MutableLiveData<String> = MutableLiveData("")
    val expenseTitle: LiveData<String> get() = _expenseTitle
    private val _totalCost: MutableLiveData<Double> = MutableLiveData(0.0)
    val totalCost: LiveData<Double> get() = _totalCost
    private val _splitMethod: MutableLiveData<ExpenseSplittingMethod> = MutableLiveData(ExpenseSplittingMethod.EQUAL)
    val splitMethod: LiveData<ExpenseSplittingMethod> get() = _splitMethod
    private val _palsList: MutableLiveData<List<Pal>> = MutableLiveData(listOf())
    val palsList: LiveData<List<Pal>> get() = _palsList
    private val _debtorIdsSet: MutableLiveData<Set<String>> = MutableLiveData(emptySet())
    val debtorIdsSet: LiveData<Set<String>> get() = _debtorIdsSet
    private val _amountsOwedMap: MutableLiveData<Map<String, Double>> = MutableLiveData(emptyMap())
    val amountsOwedMap: LiveData<Map<String, Double>> get() = _amountsOwedMap

    init {
        fetchExpenses()
        fetchPalsList()
    }

    fun getUserId(): String {
        return authRepo.getCurrentUID() ?: ""
    }

    fun fetchExpenses() {
        val userId = authRepo.getCurrentUID()
        val tripId = tripsRepo.selectedTrip.tripId
        if (userId != null && tripId != null) {
            viewModelScope.launch {
                val result = expensesRepo.fetchExpenses(userId, tripId)
                if (result != null) {
                    _expensesList.value = result!!
                }
            }
        }
    }

    private fun fetchPalsList() {
        val userId = authRepo.getCurrentUID()
        val palIds = tripsRepo.selectedTrip.tripPalIds?.toMutableList()
        if (userId != null && palIds != null) {
            palIds.remove(userId)
            viewModelScope.launch {
                _palsList.value = palsRepo.fetchPals(palIds)
            }
        }
    }

    fun settleExpense(expense: Expense) {
        val expenseId = expense.expenseId
        if (expenseId != null) {
            val updatedExpense = Expense(
                expense.title,
                expense.date,
                expense.tripId,
                expense.amountPaid,
                expense.payerId,
                expense.debtorIds,
                expense.amountsOwed,
                !(expense.settled ?: false)
            )
            viewModelScope.launch {
                expensesRepo.updateExpense(expenseId, updatedExpense)
                fetchExpenses()
            }
        }
    }

    fun clearNewExpenseForm() {
        _expenseTitle.value = ""
        setTotalCost(0.0)
        setSplitMethod(ExpenseSplittingMethod.EQUAL)
        _debtorIdsSet.value = emptySet()
        _amountsOwedMap.value = emptyMap()

        _editExpenseId = ""
        _createExpenseSuccess.value = false
        _updateNewExpenseInputs.value = true
    }

    fun editExpense(expense: Expense) {
        _expenseTitle.value = expense.title ?: ""
        setTotalCost(expense.amountPaid!!)
        setSplitMethod(ExpenseSplittingMethod.EXACT)
        _amountsOwedMap.value = expense.amountsOwed!!
        _debtorIdsSet.value = expense.debtorIds!!.toSet()

        _editExpenseId = expense.expenseId ?: ""
        _createExpenseSuccess.value = false
        _updateNewExpenseInputs.value = true
    }

    fun setTotalCost(cost: Double) {
        _totalCost.value = round(cost * 100) / 100
    }

    fun setSplitMethod(method: ExpenseSplittingMethod) {
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

    fun saveExpense(title: String) {
        viewModelScope.launch {
            val newExpense = Expense(
                title,
                Date(),
                tripsRepo.selectedTrip.tripId,
                _totalCost.value,
                authRepo.getCurrentUID(),
                _debtorIdsSet.value?.toList(),
                splitExpense(_totalCost.value!!, _amountsOwedMap.value!!.toMutableMap(), _splitMethod.value!!),
                false
            )

            if (validateExpense(newExpense)) {
                if (_editExpenseId.isNullOrEmpty()) {
                    expensesRepo.createExpense(newExpense)
                }
                else {
                    expensesRepo.updateExpense(_editExpenseId, newExpense)
                }

                _editExpenseId = ""
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
        if (expense.tripId == null) return false
        if (expense.amountPaid == null || expense.amountPaid <= 0) {
            _toastMessage.value = "Cannot have no expense total"
            return false
        }
        if (expense.payerId.isNullOrEmpty()) return false
        if (expense.debtorIds == null) return false
        if (expense.amountsOwed == null) return false
        if (expense.settled == null) return false

        if (expense.amountsOwed.size != expense.debtorIds.size || expense.amountsOwed.values.sum() > expense.amountPaid) {
            _toastMessage.value = "Total of amounts owed cannot exceed expense total"
            return false
        }

        return true
    }

    fun resetUpdateFlag() {
        _updateNewExpenseInputs.value = false
    }

    fun clearToastMessage() {
        _toastMessage.value = ""
    }
}