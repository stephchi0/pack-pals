package com.example.packpals.views.itinerary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.viewmodels.ItineraryPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItinerarySearchPageFragment: Fragment() {
    private val viewModel: ItineraryPageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itinerary_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout = requireView().findViewById<LinearLayout>(R.id.searchResults)
        viewModel.searchResultList.observe(viewLifecycleOwner) { searchResults ->
            linearLayout.removeAllViews()
            for (item in searchResults) {
                val searchView = LayoutInflater.from(context)
                    .inflate(R.layout.view_itinerary_search_result, linearLayout, false)

                searchView.findViewById<Button>(R.id.predictionButton).text = item.mainText
                searchView.findViewById<TextView>(R.id.address).text = item.secondaryText
//                searchView.setOnClickListener {
//                    viewModel.setCurrentItem(item)
//                    findNavController().navigate(R.id.action_itineraryFragment_to_itemDetailsPageFragment)
//                }
                searchView.findViewById<Button>(R.id.predictionButton).setOnClickListener {
                    lifecycleScope.launch{
                        viewModel.setSearchedItem(item.mainText!!, item.secondaryText!!)
                        viewModel.setAdd(true)
                        findNavController().navigate(R.id.action_itinerarySearchPageFragment_to_itineraryDetailsPageFragment)
                    }

                }
                linearLayout.addView(searchView)
            }
        }
    }
}