package com.example.packpals.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.packpals.R

class ProfilePageFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonEdit  = requireView().findViewById<Button>(R.id.button_edit)
        buttonEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profilePageFragment_to_profileEditFragment)
        }
    }

    companion object {
        fun newInstance() =
            ProfilePageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}