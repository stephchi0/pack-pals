package com.example.packpals.views.itinerary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.packpals.R
import com.example.packpals.viewmodels.ItineraryItemAdapter
import com.example.packpals.viewmodels.ItineraryPageViewModel

class ItineraryPageFragment : Fragment() {
    private val viewModel: ItineraryPageViewModel by viewModels()
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

        viewModel.itineraryItemsList.observe(viewLifecycleOwner) { itineraryItems ->
            itineraryItemAdapter = ItineraryItemAdapter(itineraryItems)
            val rvItineraryItems = requireView().findViewById<RecyclerView>(R.id.rvItineraryItems)
            rvItineraryItems.adapter = itineraryItemAdapter
            rvItineraryItems.layoutManager = LinearLayoutManager(context)
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