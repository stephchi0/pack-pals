package com.example.packpals.views.trips

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
import androidx.fragment.app.viewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.TripsPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTripFragment : Fragment(){
    private val viewModel: TripsPageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayout = requireView().findViewById<LinearLayout>(R.id.editTripPalsLinearLayout)

        viewModel.currentEditTripItem.observe(viewLifecycleOwner){
            requireView().findViewById<TextView>(R.id.editTripNameInput).text =
                viewModel.currentEditTripItem.value?.title ?: ""
        }
        //println(viewModel.currentEditTripItem.title)
        viewModel.palsList.observe(viewLifecycleOwner) { palsList ->
            linearLayout.removeAllViews()
            for (pal in viewModel.palsList.value!!) {
                val addPalView = LayoutInflater.from(context).inflate(R.layout.view_new_trip_add_pal, linearLayout, false)
                val checkmarkImageView = addPalView.findViewById<ImageView>(R.id.newTripCheckmarkIcon)
                addPalView.findViewById<TextView>(R.id.newTripPalName).text = pal.name
                if(viewModel.currentTripPalIds.value?.contains(pal.id)==true){
                    checkmarkImageView.setImageResource(R.drawable.ic_checked)
                }
                else{
                    checkmarkImageView.setImageResource(R.drawable.ic_unchecked)
                }

                addPalView.setOnClickListener {
                    if (pal.id != null) {
                        viewModel.addRemoveTripPal(pal.id!!)
                    }
                }
                linearLayout.addView(addPalView)
                updateTripPals()
            }
        }
        val saveTripButton = requireView().findViewById<Button>(R.id.saveChangesButton)
        saveTripButton.setOnClickListener {
            val tripName = requireView().findViewById<TextView>(R.id.editTripNameInput).text.toString()
            if (tripName.isNotEmpty()) {
                //update the trip
                viewModel.editTrip(tripName)
            }

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.tripFragmentContainerView, TripsListFragment())
            transaction.commit()
        }

        viewModel.currentTripPalIds.observe(viewLifecycleOwner){
            updateTripPals()
        }

        val leaveTripButton = requireView().findViewById<Button>(R.id.leaveTripButton)
        leaveTripButton.setOnClickListener{
            val userId = viewModel.getUserId()
            val selectedTrip = viewModel.getCurrentEditTrip()
            if (selectedTrip != null) {
                print("hello")
                viewModel.leaveTrip(userId,selectedTrip)
            }
            //return back to tripslist
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.tripFragmentContainerView, TripsListFragment())
            transaction.commit()
        }

    }

    private fun updateTripPals(){
        val linearLayout = requireView().findViewById<LinearLayout>(R.id.editTripPalsLinearLayout)
        for (i in 0 until linearLayout.childCount){
            val addPalView = linearLayout.getChildAt(i)
            val checkmarkImageView = addPalView.findViewById<ImageView>(R.id.newTripCheckmarkIcon)
            val palId = viewModel.palsList.value?.get(i)?.id

            if(viewModel.currentTripPalIds.value?.contains(palId)==true){
                checkmarkImageView.setImageResource(R.drawable.ic_checked)
            }
            else{
                checkmarkImageView.setImageResource(R.drawable.ic_unchecked)
            }
        }
    }
}