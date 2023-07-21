package com.example.packpals.views.itinerary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.packpals.R
import com.example.packpals.viewmodels.ItineraryPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class ItemDetailPageFragment : Fragment() {
    private val viewModel: ItineraryPageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_details_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentItem.observe(viewLifecycleOwner) { item ->
            val tvLocationField = view.findViewById<TextView>(R.id.tvLocationField)
            val tvDateField = view.findViewById<TextView>(R.id.tvDateField)
            val tvForecastField = view.findViewById<TextView>(R.id.tvForecastField)

            tvLocationField.text = item.location
            tvDateField.text = SimpleDateFormat("MM/dd/yyyy").format(item.startDate)
            tvForecastField.text = item.forecast
        }
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