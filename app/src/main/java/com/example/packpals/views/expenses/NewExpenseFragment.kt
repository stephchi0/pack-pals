package com.example.packpals.views.expenses

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel.debtorIdsSet.observe(viewLifecycleOwner) { debtorIds ->
            for (i in 0 until linearLayout.childCount) {
                val addPalView = linearLayout.getChildAt(i)

                val palId = viewModel.palsList.value?.get(i)?.id
                if (debtorIds.contains(palId)) {
                    addPalView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darker_blue))
                    addPalView.findViewById<LinearLayout>(R.id.amountOwedExpenseLinearLayout).visibility = View.VISIBLE

                    val amountOwedEditText = addPalView.findViewById<EditText>(R.id.amountOwedExpenseEditText)
                    amountOwedEditText.setText(String.format("%.2f", viewModel.amountsOwedMap.value!![palId]))
                }
                else {
                    addPalView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                    addPalView.findViewById<LinearLayout>(R.id.amountOwedExpenseLinearLayout).visibility = View.GONE
                }
            }
        }

        val createExpenseButton = requireView().findViewById<Button>(R.id.saveExpenseButton)
        createExpenseButton.setOnClickListener {
            val txtExpenseName = requireView().findViewById<TextInputEditText>(R.id.expenseNameInput).text.toString()
            if (txtExpenseName.isNotEmpty()) {
                viewModel.createExpense(txtExpenseName, Date())
                findNavController().navigate(R.id.action_newExpenseFragment_to_expensesFragment)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NewExpenseFragment().apply {

            }
    }
}