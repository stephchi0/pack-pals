package com.example.packpals.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.viewmodels.ProfilePageViewModel

class ProfilePageFragment : Fragment() {
    private val viewModel: ProfilePageViewModel by viewModels()

    private lateinit var name: TextView
    private lateinit var bio: TextView
    private lateinit var joinedDate : TextView
    private lateinit var genderAge : TextView
    private lateinit var palId: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        name = requireView().findViewById(R.id.nameView)
        bio = requireView().findViewById(R.id.bioView)
        joinedDate = requireView().findViewById(R.id.joinedView)
        genderAge = requireView().findViewById(R.id.genderAgeView)
        palId = requireView().findViewById(R.id.idView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                name.text = profile.name
                genderAge.text = profile.gender +", " + profile.age.toString()

            }
        }

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