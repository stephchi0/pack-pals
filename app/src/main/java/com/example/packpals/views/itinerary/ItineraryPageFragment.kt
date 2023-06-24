package com.example.packpals.views.itinerary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.packpals.R
import com.example.packpals.models.Itinerary_Item
import com.example.packpals.viewmodels.ItineraryItemAdapter

class ItineraryPageFragment : Fragment() {
    private lateinit var itineraryItemAdapter: ItineraryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itinerary_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itineraryItemAdapter = ItineraryItemAdapter(mutableListOf(
            Itinerary_Item("Fenugs Chasm", " May 1st, 2023 12:30pm-2pm", "Forecast: Rainy, 19 degrees Celsius"),
            Itinerary_Item("Anzac Hill of Chi", "May 2nd, 2023 2pm-5pm", "Forecast: Partly Cloudy, 19 degrees Celsius"),
            Itinerary_Item("Mount Spooder", "May 3rd, 2023 2pm-5pm", "Forecast: Sunny, 19 degrees Celsius")
        ))

        val rvItineraryItems = requireView().findViewById<RecyclerView>(R.id.rvItineraryItems)
        rvItineraryItems.adapter = itineraryItemAdapter
        rvItineraryItems.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItineraryPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}