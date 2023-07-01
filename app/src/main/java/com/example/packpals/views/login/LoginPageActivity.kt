package com.example.packpals.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.packpals.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
    }
}