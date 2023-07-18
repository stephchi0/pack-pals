package com.example.packpals.views.itinerary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.packpals.R
import com.example.packpals.viewmodels.AddItineraryItemPageViewModel
import com.example.packpals.viewmodels.ItineraryPageViewModel

class AddItineraryPageFragment : Fragment() {
//    private val viewModel: AddItineraryItemPageViewModel by viewModels()
    private val viewModel : ItineraryPageViewModel by viewModels()

    private lateinit var itineraryItemAdapter: AddItineraryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_itinerary_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.reccItineraryItemsList.observe(viewLifecycleOwner) { itineraryItems ->
//            itineraryItemAdapter = AddItineraryItemAdapter(itineraryItems, viewModel.itineraryItemsList.value!! )
//            itineraryItemAdapter.updateLists(itineraryItems, viewModel.itineraryItemsList.value!!)
//            val rvItineraryItems = requireView().findViewById<RecyclerView>(R.id.lliterinerary)
//            rvItineraryItems.adapter = itineraryItemAdapter
//            rvItineraryItems.layoutManager = LinearLayoutManager(context)
//
//        }

//        viewModel.itineraryItemsList.observe(viewLifecycleOwner) { itineraryItems ->
//            itineraryItemAdapter = AddItineraryItemAdapter(itineraryItems, viewModel.reccItineraryItemsList.value!! )
//            itineraryItemAdapter.updateLists(itineraryItems, viewModel.itineraryItemsList.value!!)
//            val rvItineraryItems = requireView().findViewById<RecyclerView>(R.id.lliterinerary)
//            rvItineraryItems.adapter = itineraryItemAdapter
//            rvItineraryItems.layoutManager = LinearLayoutManager(context)
//        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddItineraryPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}