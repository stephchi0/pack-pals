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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.viewmodels.ItineraryPageViewModel

class ItineraryPageFragment : Fragment() {
    private val viewModel: ItineraryPageViewModel by viewModels()

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

        val linearLayout = requireView().findViewById<LinearLayout>(R.id.lliterinerary)
        viewModel.itineraryItemsList.observe(viewLifecycleOwner) { itineraryItems ->
            linearLayout.removeAllViews()
            for ((location, date, forecast) in itineraryItems) {
                val itineraryView = LayoutInflater.from(context).inflate(R.layout.view_itenerary_item, linearLayout, false)

                itineraryView.findViewById<TextView>(R.id.tvlocation).text = location
                itineraryView.findViewById<TextView>(R.id.tvdate).text = date
                itineraryView.findViewById<TextView>(R.id.tvforecast).text = forecast
                itineraryView.findViewById<ImageView>(R.id.image).setImageResource(R.mipmap.fenugs)

                itineraryView.setOnClickListener {
                    // fill in later
                }

                linearLayout.addView(itineraryView)
            }
        }

        val addNewItemButton = requireView().findViewById<Button>(R.id.addItemButton)
        addNewItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_itineraryFragment_to_addItineraryItemFragment)
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