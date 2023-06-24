package com.example.packpals.views.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.ExpensesPageViewModel
import java.util.Date

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

        val linearLayout = requireView().findViewById<LinearLayout>(R.id.newExpenseLinearLayout)
        viewModel.payingPalIds.observe(viewLifecycleOwner) { payingPalIds ->
            for (pal in viewModel.palsList.value!!) {
                val addPalView = LayoutInflater.from(context).inflate(R.layout.view_add_pal, linearLayout, false)

                addPalView.findViewById<TextView>(R.id.palName).text = pal.name
                if (pal.id in payingPalIds) {
                    // style selected paying pals differently
                }
                addPalView.setOnClickListener {
                    if (pal.id != null) {
                        viewModel.addRemovePayingPal(pal.id)
                    }
                }

                linearLayout.addView(addPalView)
            }
        }
        val createExpenseButton = requireView().findViewById<Button>(R.id.createExpenseButton)
        createExpenseButton.setOnClickListener {
            val txtExpenseName = requireView().findViewById<TextView>(R.id.expenseNameInput).text.toString()
            val txtTotalCost = requireView().findViewById<TextView>(R.id.totalCostInput).text.toString().toDoubleOrNull()
            if (txtExpenseName.isNotEmpty() && txtTotalCost != null) {
                viewModel.createExpense(txtExpenseName, Date(), txtTotalCost)
            }

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.expensesFragmentContainerView, ExpenseListFragment())
            transaction.commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NewExpenseFragment().apply {

            }
    }
}