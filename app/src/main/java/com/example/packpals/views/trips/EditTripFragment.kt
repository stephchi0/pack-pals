package com.example.packpals.views.trips

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.TripsPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTripFragment : Fragment(){
    private val viewModel: TripsPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout = requireView().findViewById<LinearLayout>(R.id.tripPalsLinearLayout)
        viewModel.currentTripPalIds.observe(viewLifecycleOwner) { currentTripPalIds ->
            for (pal in viewModel.palsList.value!!) {
                val addPalView = LayoutInflater.from(context).inflate(R.layout.view_new_expense_add_pal, linearLayout, false) // probably need to change this view, currently has expense specific UI

                addPalView.findViewById<TextView>(R.id.palName).text = pal.name
                addPalView.setOnClickListener {
                    if (pal.id != null) {
                        viewModel.addRemoveTripPal(pal.id!!)
                    }
                }
                linearLayout.addView(addPalView)
            }
        }
        val createTripButton = requireView().findViewById<Button>(R.id.createTripButton)
        createTripButton.setOnClickListener {
            val tripName = requireView().findViewById<TextView>(R.id.tripNameInput).text.toString()
            if (tripName.isNotEmpty()) {
                viewModel.createTrip(tripName)
            }

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.tripFragmentContainerView, TripsListFragment())
            transaction.commit()
        }
    }
}