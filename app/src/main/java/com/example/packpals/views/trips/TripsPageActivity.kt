package com.example.packpals.views.trips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.TripsPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips_page)
    }
}