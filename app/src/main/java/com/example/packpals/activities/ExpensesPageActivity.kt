package com.example.packpals.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.packpals.R
import com.example.packpals.viewmodels.ExpensesPageViewModel

class ExpensesPageActivity : AppCompatActivity() {
    private val viewModel: ExpensesPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_page)

        val linearLayout = findViewById<LinearLayout>(R.id.expensesLinearLayout)
        viewModel.expensesList.observe(this) { expensesList ->
            for (expense in expensesList) {
                val expenseView = LayoutInflater.from(this).inflate(R.layout.view_expense, linearLayout, false)

                expenseView.findViewById<TextView>(R.id.expenseTitle).text = expense.title
                expenseView.findViewById<TextView>(R.id.expenseDate).text = expense.date
                expenseView.findViewById<TextView>(R.id.expenseAmountOwed).text = String.format("You owe $%.2f", expense.amountOwed)

                expenseView.setOnClickListener {
                    println(expense.title)
                }

                linearLayout.addView(expenseView)
            }
        }

        val addNewExpenseButton = findViewById<Button>(R.id.addNewExpenseButton)
        addNewExpenseButton.setOnClickListener {
            val intent = Intent(this, NewExpensePageActivity::class.java)
            startActivity(intent)
        }
    }
}