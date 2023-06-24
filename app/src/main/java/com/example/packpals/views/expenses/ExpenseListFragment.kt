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
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.viewmodels.ExpensesPageViewModel
import java.text.SimpleDateFormat

class ExpenseListFragment : Fragment() {
    private val viewModel: ExpensesPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout = requireView().findViewById<LinearLayout>(R.id.expensesLinearLayout)
        viewModel.expensesList.observe(viewLifecycleOwner) { expensesList ->
            for (expense in expensesList) {
                val expenseView = LayoutInflater.from(context).inflate(R.layout.view_expense, linearLayout, false)

                expenseView.findViewById<TextView>(R.id.expenseTitle).text = expense.title
                expenseView.findViewById<TextView>(R.id.expenseDate).text = SimpleDateFormat("MM/dd/yyyy").format(expense.date)
                // add some logic here for a different message if user id matches payer id
                expenseView.findViewById<TextView>(R.id.expenseAmountOwed).text = String.format("You owe $%.2f", expense.amountPaid)

                expenseView.setOnClickListener {
                    // fill in later with expense details
                }

                linearLayout.addView(expenseView)
            }
        }

        val addNewExpenseButton = requireView().findViewById<Button>(R.id.addNewExpenseButton)
        addNewExpenseButton.setOnClickListener {
            findNavController().navigate(R.id.action_expensesFragment_to_newExpenseFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExpenseListFragment().apply {

            }
    }
}