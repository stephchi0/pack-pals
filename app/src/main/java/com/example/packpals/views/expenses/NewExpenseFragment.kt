package com.example.packpals.views.expenses

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.viewmodels.ExpensesPageViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class NewExpenseFragment : Fragment() {
    private val viewModel: ExpensesPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage()
            }
        }

        val totalCostEditText = requireView().findViewById<TextInputEditText>(R.id.expenseTotalInput)
        totalCostEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newTotalCost = s.toString().toDoubleOrNull()
                if (newTotalCost != null) {
                    viewModel.setTotalCost(newTotalCost)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val splitBySpinner = requireView().findViewById<Spinner>(R.id.splitBySpinner)
        if (splitBySpinner != null) {
            val spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.split_by_spinner_values,
                android.R.layout.simple_spinner_dropdown_item // TODO: create custom layout for spinner dropdown item
            )
            splitBySpinner.adapter = spinnerAdapter
            splitBySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val newSplitMethod = ExpensesPageViewModel.ExpenseSplittingMethod.values()[position]
                    viewModel.setSplitMethod(newSplitMethod)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        val linearLayout = requireView().findViewById<LinearLayout>(R.id.membersToSplitLinearLayout)
        viewModel.palsList.observe(viewLifecycleOwner) { palsList ->
            linearLayout.removeAllViews()
            for (pal in palsList) {
                val addPalView = LayoutInflater.from(context).inflate(R.layout.view_new_expense_add_pal, linearLayout, false)
                addPalView.findViewById<TextView>(R.id.palName).text = pal.name
                addPalView.setOnClickListener {
                    if (pal.id != null) {
                        viewModel.addRemovePayingPal(pal.id)
                    }
                }

                addPalView.findViewById<EditText>(R.id.amountOwedExpenseEditText).addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val newAmountOwed = s.toString().toDoubleOrNull()
                        if (newAmountOwed != null && pal.id != null) {
                            viewModel.setAmountOwed(pal.id, newAmountOwed)
                        }
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                linearLayout.addView(addPalView)
            }
        }
        viewModel.debtorIdsSet.observe(viewLifecycleOwner) {
            updateAddPalCards()
        }
        viewModel.splitMethod.observe(viewLifecycleOwner) {
            updateAddPalCards()
        }

        val createExpenseButton = requireView().findViewById<Button>(R.id.saveExpenseButton)
        createExpenseButton.setOnClickListener {
            val txtExpenseName = requireView().findViewById<TextInputEditText>(R.id.expenseNameInput).text.toString()
            viewModel.createExpense(txtExpenseName, Date())
        }

        viewModel.createExpenseSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_newExpenseFragment_to_expensesFragment)
            }
        }
    }

    private fun updateAddPalCards() {
        val linearLayout = requireView().findViewById<LinearLayout>(R.id.membersToSplitLinearLayout)
        for (i in 0 until linearLayout.childCount) {
            val addPalView = linearLayout.getChildAt(i)
            val percentSignText = addPalView.findViewById<TextView>(R.id.percentSignText)
            val amountOwedEditText = addPalView.findViewById<EditText>(R.id.amountOwedExpenseEditText)
            val checkmarkImageView = addPalView.findViewById<ImageView>(R.id.checkmarkIcon)

            val palId = viewModel.palsList.value?.get(i)?.id
            if (viewModel.debtorIdsSet.value?.contains(palId) == true) {
                checkmarkImageView.setImageResource(R.drawable.ic_checked)

                when (viewModel.splitMethod.value) {
                    ExpensesPageViewModel.ExpenseSplittingMethod.PERCENTAGE -> {
                        amountOwedEditText.visibility = View.VISIBLE
                        percentSignText.visibility = View.VISIBLE
                        amountOwedEditText.setText(viewModel.amountsOwedMap.value!![palId].toString())
                    }
                    ExpensesPageViewModel.ExpenseSplittingMethod.EXACT -> {
                        amountOwedEditText.visibility = View.VISIBLE
                        percentSignText.visibility = View.GONE
                        amountOwedEditText.setText(String.format("%.2f",
                            viewModel.amountsOwedMap.value!![palId]
                        ))
                    }
                    else -> {
                        amountOwedEditText.visibility = View.GONE
                        percentSignText.visibility = View.GONE
                    }
                }
            }
            else {
                checkmarkImageView.setImageResource(R.drawable.ic_unchecked)
                amountOwedEditText.visibility = View.GONE
                percentSignText.visibility = View.GONE
            }
        }
    }
}