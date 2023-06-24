package com.example.packpals.views.itinerary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.packpals.R
import com.example.packpals.models.Itinerary_Item
import com.example.packpals.viewmodels.ItineraryItemAdapter
import kotlinx.android.synthetic.main.activity_itinerary_page.rvItineraryItems

class ItineraryPageActivity : AppCompatActivity() {

    private lateinit var itineraryItemAdapter: ItineraryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerary_page)

        itineraryItemAdapter = ItineraryItemAdapter(mutableListOf(
            Itinerary_Item("Fenugs Chasm", " May 1st, 2023 12:30pm-2pm", "Forecast: Rainy, 19 degrees Celsius"),
            Itinerary_Item("Anzac Hill of Chi", "May 2nd, 2023 2pm-5pm", "Forecast: Partly Cloudy, 19 degrees Celsius"),
            Itinerary_Item("Mount Spooder", "May 3rd, 2023 2pm-5pm", "Forecast: Sunny, 19 degrees Celsius")
        ))

        rvItineraryItems.adapter = itineraryItemAdapter
        rvItineraryItems.layoutManager = LinearLayoutManager(this)
    }
}