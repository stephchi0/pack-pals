package com.example.packpals.views.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.packpals.R


class LocationDetails : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationStartDate = view.findViewById<TextView>(R.id.locationStartDate)
        locationStartDate.text = "Start Date: " + arguments?.getString("locationStartDate")
        val locationEndDate = view.findViewById<TextView>(R.id.locationEndDate)
        locationEndDate.text = "End Date: " + arguments?.getString("locationEndDate")
        val locationForecast = view.findViewById<TextView>(R.id.locationForecast)
        locationForecast.text = "Forecast: " + arguments?.getString("locationForecast")
        val locationDetailsImage = view.findViewById<ImageView>(R.id.locationDetailsImage)
        Glide
            .with(requireContext())
            .load(arguments?.getString("locationPhoto"))
            .into(locationDetailsImage)
    }
}