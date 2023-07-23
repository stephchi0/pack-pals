package com.example.packpals.views.trips

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.TripsPageViewModel
import com.example.packpals.views.NavigationDrawerViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripsListFragment : Fragment() {
    private val viewModel: TripsPageViewModel by activityViewModels()

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

    override fun onResume() {
        super.onResume()
        viewModel.fetchTrips()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val upcomingTripsLayout = requireView().findViewById<LinearLayout>(R.id.activeTripsLinearLayout)
        viewModel.tripsList.observe(viewLifecycleOwner) { tripsList ->
            upcomingTripsLayout.removeAllViews()
            for (trip in tripsList) {
                if(trip.active == true){
                    val tripView = LayoutInflater.from(context).inflate(R.layout.view_trip, upcomingTripsLayout, false)
                    val activeMenuButton = tripView.findViewById<ImageButton>(R.id.trip_triple_dot_menu)
                    tripView.findViewById<TextView>(R.id.tripTitle).text = trip.title

                    val tripItem = tripView.findViewById<LinearLayout>(R.id.tripLinearLayout)
                    tripItem.setOnClickListener{
                        viewModel.selectTrip(trip)

                        val intent = Intent(activity, NavigationDrawerViewActivity::class.java)
                        startActivity(intent)
                    }

                    activeMenuButton.setOnClickListener{
                        val popup = PopupMenu(context, activeMenuButton)
                        popup.menuInflater.inflate(R.menu.active_trip_settings, popup.menu)
                        popup.setOnMenuItemClickListener { menuItem ->
                            val id = menuItem.itemId
                            if(id == R.id.active_trip_edit_item){
                                viewModel.setCurrentEditTrip(trip)
                                val transaction = parentFragmentManager.beginTransaction()
                                transaction.replace(R.id.tripFragmentContainerView, EditTripFragment())
                                transaction.commit()

                            }
                            else if (id ==R.id.active_trip_archive_item){
                                viewModel.editActive(trip)
                                viewModel.fetchTrips()
                            }

                            false
                        }
                        popup.show()
                    }

                    upcomingTripsLayout.addView(tripView)
                }
            }
        }

        val pastTripsLayout = requireView().findViewById<LinearLayout>(R.id.pastTripsLinearLayout)
        viewModel.tripsList.observe(viewLifecycleOwner) { tripsList ->
            pastTripsLayout.removeAllViews()
            for (trip in tripsList) {
                if(trip.active == false){
                    val tripView = LayoutInflater.from(context).inflate(R.layout.view_trip, pastTripsLayout, false)
                    val pastMenuButton = tripView.findViewById<ImageButton>(R.id.trip_triple_dot_menu)
                    tripView.findViewById<TextView>(R.id.tripTitle).text = trip.title

                    val tripItem = tripView.findViewById<LinearLayout>(R.id.tripLinearLayout)
                    tripItem.setOnClickListener{
                        val intent = Intent(activity, NavigationDrawerViewActivity::class.java)
                        startActivity(intent)
                    }
                    pastMenuButton.setOnClickListener{
                        val popup = PopupMenu(context, pastMenuButton)
                        popup.menuInflater.inflate(R.menu.past_trip_settings, popup.menu)
                        popup.setOnMenuItemClickListener { menuItem ->
                            val id = menuItem.itemId
                            if(id == R.id.past_trip_edit_item){
                                viewModel.setCurrentEditTrip(trip)
                                val transaction = parentFragmentManager.beginTransaction()
                                transaction.replace(R.id.tripFragmentContainerView, EditTripFragment())
                                transaction.commit()
                            }
                            else if (id ==R.id.past_trip_make_active_item){
                                viewModel.editActive(trip)
                                viewModel.fetchTrips()
                            }

                            false
                        }
                        popup.show()
                    }

                    pastTripsLayout.addView(tripView)
                }
            }
        }

        val viewCurrentTripButton = requireView().findViewById<Button>(R.id.viewCurrentTripButton)
        viewCurrentTripButton.setOnClickListener {
            val intent = Intent(activity, NavigationDrawerViewActivity::class.java)
            startActivity(intent)
        }

        val newTripButton = requireView().findViewById<Button>(R.id.addNewTripButton)
        newTripButton.setOnClickListener {
            viewModel.clearTripItems()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.tripFragmentContainerView, NewTripFragment())
            transaction.commit()
        }
    }
}