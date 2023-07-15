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
                    amountOwedView.text = String.format("You owe: $%.2f", expense.amountsOwed!![userId]) // TODO: add payer's name
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