package com.example.packpals.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.NewExpensePageViewModel

class NewExpensePageActivity : AppCompatActivity() {
    private val viewModel: NewExpensePageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expense_page)

        val linearLayout = findViewById<LinearLayout>(R.id.newExpenseLinearLayout)
        viewModel.palsList.observe(this) { palsList ->
            for (pal in palsList) {
                val addPalView = LayoutInflater.from(this).inflate(R.layout.view_add_pal, linearLayout, false)

                addPalView.findViewById<TextView>(R.id.palName).text = pal.name

                addPalView.setOnClickListener {
                    println(pal.name)
                }

                linearLayout.addView(addPalView)
            }
        }

        val createExpenseButton = findViewById<Button>(R.id.createExpenseButton)
        createExpenseButton.setOnClickListener {
            finish()
        }
    }
}