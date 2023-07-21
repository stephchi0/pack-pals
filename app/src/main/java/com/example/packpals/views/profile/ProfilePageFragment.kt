package com.example.packpals.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.R.id
import com.example.packpals.viewmodels.ProfilePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = requireView().findViewById(R.id.nameView123)
        bio = requireView().findViewById(R.id.bioView)
        joinedDate = requireView().findViewById(R.id.joinedView)
        genderAge = requireView().findViewById(R.id.genderAgeView)
        palId = requireView().findViewById(R.id.idView)

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                name.text = profile.name
                genderAge.text = profile.gender
                bio.text = profile.bio
                palId.text = profile.id
            }
        }
        viewModel.fetchProfile()

        val buttonEdit: ImageView = requireView().findViewById(R.id.button_edit)
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