package com.example.packpals.views.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.packpals.viewmodels.ProfilePageViewModel
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {
    private val viewModel: ProfilePageViewModel by viewModels()
    private val selectImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                viewModel.setProfilePictureUri(imageUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = requireView().findViewById<EditText>(R.id.nameEdit)
        val bio = requireView().findViewById<EditText>(R.id.bioEdit)
        val update = requireView().findViewById<Button>(R.id.updateProfile)
        val profilePicture = requireView().findViewById<ShapeableImageView>(R.id.profileEditProfilePicture)

        profilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImage.launch(intent)
        }

        update.setOnClickListener { //on clicking update profile button, update all information
            val nameString = name.text.toString()
            val bioString = bio.text.toString()

            viewModel.updateProfile(nameString, bioString)
            findNavController().navigate(R.id.action_profileEditFragment_to_profilePageFragment)
        }

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            name.setText(profile?.name)
            bio.setText(profile?.bio)
            if (!profile?.profilePictureURL.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(profile.profilePictureURL)
                    .into(profilePicture)
            }
        }

        viewModel.profilePictureUri.observe(viewLifecycleOwner) { profilePictureUri ->
            if (profilePictureUri != Uri.EMPTY) {
                Glide.with(requireContext())
                    .load(profilePictureUri)
                    .into(profilePicture)
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileEditFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}