package com.example.packpals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.packpals.views.login.LoginPageActivity
import com.example.packpals.views.trips.TripsPageActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Firebase.auth.currentUser != null) {
            val intent = Intent(this, TripsPageActivity::class.java)
            startActivity(intent)
        }

        val addStartButton = findViewById<Button>(R.id.start_button)
        addStartButton.setOnClickListener{
            val intent = Intent(this, LoginPageActivity::class.java)
            startActivity(intent)
        }
    }


}