package com.example.packpals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.packpals.models.Itinerary_Item
import com.example.packpals.viewmodels.ItineraryItemAdapter
import kotlinx.android.synthetic.main.activity_itinerary_page.rvItineraryItems

class ItineraryPageActivity : AppCompatActivity() {

    private lateinit var itineraryItemAdapter: ItineraryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}