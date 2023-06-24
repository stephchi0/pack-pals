package com.example.packpals.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.packpals.R
import android.content.Intent

class ProfileEditActivity : AppCompatActivity (){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val buttonEdit  = findViewById<Button>(R.id.button_exit)
        buttonEdit.setOnClickListener {
            val intent = Intent(this@ProfileEditActivity, ProfilePageActivity::class.java)
            startActivity(intent)
        }
    }
}