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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.packpals.R
import com.example.packpals.viewmodels.ItineraryPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class ItineraryPageFragment : Fragment() {
    private val viewModel: ItineraryPageViewModel by activityViewModels()

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
            for (item in itineraryItems) {
                val itineraryView = LayoutInflater.from(context).inflate(R.layout.view_itenerary_item, linearLayout, false)

                itineraryView.findViewById<TextView>(R.id.tvlocation).text = item.location
                itineraryView.findViewById<TextView>(R.id.tvforecast).text = item.forecast
                itineraryView.findViewById<TextView>(R.id.tvdate).text = SimpleDateFormat("MM/dd/yyyy").format(item.startDate)

                if(item.photo_reference != null){
                    Glide
                        .with(requireContext())
                        .load(item.photo_reference)
                        .into(itineraryView.findViewById(R.id.image))
                }else{
                    itineraryView.findViewById<ImageView>(R.id.image).setImageResource(R.mipmap.fenugs)
                }


                itineraryView.setOnClickListener {
                    viewModel.setCurrentItem(item)
                    findNavController().navigate(R.id.action_itineraryFragment_to_itemDetailsPageFragment)
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