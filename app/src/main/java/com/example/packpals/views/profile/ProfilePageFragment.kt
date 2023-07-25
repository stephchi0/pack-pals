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
    private lateinit var name: TextView
    private lateinit var bio: TextView
    private lateinit var palId: TextView
    private lateinit var profilePicture: ImageView

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
        palId = requireView().findViewById(R.id.idView)
        profilePicture = requireView().findViewById(R.id.profilePageProfilePicture)

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                name.text = profile.name
                bio.text = profile.bio
                palId.text = profile.id
                if (!profile.profilePictureURL.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(profile.profilePictureURL)
                        .into(profilePicture)
                }
            }
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