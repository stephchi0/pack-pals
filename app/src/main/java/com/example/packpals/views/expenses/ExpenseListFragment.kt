package com.example.packpals.views.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.viewmodels.ExpensesPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class ExpenseListFragment : Fragment() {
    private val viewModel: ExpensesPageViewModel by activityViewModels()

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

        val totalExpensesButton = requireView().findViewById<Button>(R.id.totalExpensesButton)
        totalExpensesButton.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_total_expenses, null, false)
            val dialogTextView = dialogView.findViewById<TextView>(R.id.totalExpensesTextView)

            val totalExpensesOwed = viewModel.getTotalExpenses()
            var totalExpensesText = ""
            for (pal in viewModel.palsList.value ?: emptyList()) {
                val amountOwed = totalExpensesOwed[pal.id] ?: 0.0
                if (amountOwed >= 0.0) {
                    totalExpensesText += String.format("\n%s owes you: $%.2f\n", pal.name, amountOwed)
                }
                else {
                    totalExpensesText += String.format("\nYou owe %s: $%.2f\n", pal.name, -amountOwed)
                }
            }
            dialogTextView.text = totalExpensesText

            val builder = AlertDialog.Builder(requireContext())
            builder.setView(dialogView)
                .setTitle("Total Expenses Owed")
                .setPositiveButton("Close") {
                        dialog, _ -> dialog.dismiss()
                }

            builder.create().show()
        }

        val linearLayout = requireView().findViewById<LinearLayout>(R.id.expensesLinearLayout)
        viewModel.expensesList.observe(viewLifecycleOwner) { expensesList ->
            linearLayout.removeAllViews()
            val userId = viewModel.getUserId()
            for (expense in expensesList) {
                val expenseCardView = LayoutInflater.from(context).inflate(R.layout.view_expense, linearLayout, false) as CardView
                val titleView = expenseCardView.findViewById<TextView>(R.id.expenseTitle)
                val dateView = expenseCardView.findViewById<TextView>(R.id.expenseDate)
                val amountOwedView = expenseCardView.findViewById<TextView>(R.id.expenseAmountOwed)
                val editImageView = expenseCardView.findViewById<ImageView>(R.id.editImageView)
                val checkmarkImageView = expenseCardView.findViewById<ImageView>(R.id.checkmarkImageView)

                expenseCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(),
                    if (expense.settled == true) R.color.gray
                    else R.color.white
                ))
                titleView.text = expense.title
                dateView.text = SimpleDateFormat("MM/dd/yyyy").format(expense.date)
                if (expense.payerId == userId) {
                    amountOwedView.text = String.format("You are owed: $%.2f", expense.amountsOwed!!.values.sum())
                    editImageView.visibility =
                        if (expense.settled == true) View.GONE
                        else View.VISIBLE
                    checkmarkImageView.visibility = View.VISIBLE

                    checkmarkImageView.setImageResource(
                        if (expense.settled == true) R.drawable.ic_checked
                        else R.drawable.ic_unchecked
                    )
                }
                else {
                    val payer = viewModel.palsList.value?.find { pal -> pal.id == expense.payerId }
                    if (payer != null) {
                        amountOwedView.text = String.format("You owe %s: $%.2f", payer.name, expense.amountsOwed!![userId])
                    }
                    else {
                        amountOwedView.text = String.format("You owe: $%.2f", expense.amountsOwed!![userId])
                    }
                    editImageView.visibility = View.INVISIBLE
                    checkmarkImageView.visibility = View.INVISIBLE
                }

                editImageView.setOnClickListener {
                    findNavController().navigate(R.id.action_expensesFragment_to_newExpenseFragment)
                    viewModel.editExpense(expense)
                }

                checkmarkImageView.setOnClickListener {
                    viewModel.settleExpense(expense)
                }

                linearLayout.addView(expenseCardView)
            }
        }

        val addNewExpenseButton = requireView().findViewById<Button>(R.id.addNewExpenseButton)
        addNewExpenseButton.setOnClickListener {
            findNavController().navigate(R.id.action_expensesFragment_to_newExpenseFragment)
            viewModel.clearNewExpenseForm()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchExpenses()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExpenseListFragment().apply {

            }
    }
}