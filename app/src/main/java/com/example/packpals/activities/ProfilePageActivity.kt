package com.example.packpals.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.packpals.R
import android.content.Intent

class ProfilePageActivity : AppCompatActivity (){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val buttonEdit  = findViewById<Button>(R.id.button_edit)
        buttonEdit.setOnClickListener {
            val intent = Intent(this@ProfilePageActivity, ProfileEditActivity::class.java)
            startActivity(intent)
        }
    }
}