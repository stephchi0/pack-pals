package com.example.packpals.views.trips

import android.content.Intent
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
import com.example.packpals.views.NavigationDrawerViewActivity
import com.example.packpals.views.login.LoginPageActivity

class  TripsListFragment : Fragment() {
    private val viewModel: TripsPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trips_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val upcomingTripsLayout = requireView().findViewById<LinearLayout>(R.id.upcomingTripsLinearLayout)
        viewModel.tripsList.observe(viewLifecycleOwner) { tripsList ->
            for (trip in tripsList) {
                print(trip.title)
                val tripView =
                    LayoutInflater.from(context).inflate(R.layout.view_trip, upcomingTripsLayout, false)

                tripView.findViewById<TextView>(R.id.tripTitle).text = trip.title
                upcomingTripsLayout.addView(tripView)
            }
        }

        val viewCurrentTripButton = requireView().findViewById<Button>(R.id.viewCurrentTripButton)
        viewCurrentTripButton.setOnClickListener {
            val intent = Intent(activity, NavigationDrawerViewActivity::class.java)
            startActivity(intent)
        }

        val newTripButton = requireView().findViewById<Button>(R.id.addNewTripButton)
        newTripButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.tripFragmentContainerView, NewTripFragment())
            transaction.commit()
        }
    }

}