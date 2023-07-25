package com.example.packpals.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.packpals.R
import com.example.packpals.R.id
import com.example.packpals.viewmodels.ProfilePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilePageFragment : Fragment() {
    private val viewModel: ProfilePageViewModel by viewModels()
    private lateinit var nameTextView: TextView
    private lateinit var bioTextView: TextView
    private lateinit var palIdTextView: TextView
    private lateinit var profilePictureImageView: ImageView
    private lateinit var numberOfTripsCompletedTextView: TextView
    private lateinit var numberOfPalsTextView: TextView

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

        nameTextView = requireView().findViewById(R.id.nameView123)
        bioTextView = requireView().findViewById(R.id.bioView)
        palIdTextView = requireView().findViewById(R.id.idView)
        profilePictureImageView = requireView().findViewById(R.id.profilePageProfilePicture)
        numberOfTripsCompletedTextView = requireView().findViewById(R.id.numberOfTripsCompletedTextView)
        numberOfPalsTextView = requireView().findViewById(R.id.numberOfPalsTextView)

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                nameTextView.text = profile.name
                bioTextView.text = profile.bio
                palIdTextView.text = profile.id
                numberOfPalsTextView.text = (profile.pals?.size ?: 0).toString()
                if (!profile.profilePictureURL.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(profile.profilePictureURL)
                        .into(profilePictureImageView)
                }
            }
        }
        viewModel.numberOfTripsCompleted.observe(viewLifecycleOwner) { numberOfTripsCompleted ->
            numberOfTripsCompletedTextView.text = numberOfTripsCompleted.toString()
        }

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