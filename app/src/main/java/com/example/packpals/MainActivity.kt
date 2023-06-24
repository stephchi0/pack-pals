package com.example.packpals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.activity.viewModels
import com.example.packpals.R
import com.example.packpals.activities.LoginPageActivity
import com.example.packpals.viewmodels.ExpensesPageViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addStartButton = findViewById<Button>(R.id.start_button)

        addStartButton.setOnClickListener{
            val intent = Intent(this, LoginPageActivity::class.java)
            startActivity(intent)
        }
    }
}