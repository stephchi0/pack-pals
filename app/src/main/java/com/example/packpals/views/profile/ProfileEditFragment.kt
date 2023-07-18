package com.example.packpals.views.profile

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Spinner
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import android.widget.EditText
import android.widget.LinearLayout
import com.example.packpals.viewmodels.ProfilePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {
    private val viewModel: ProfilePageViewModel by viewModels()
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

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            // Handle the profile data here, update UI, etc.
        }
        var genderSelected: String? = null
        val genderSpinner: Spinner = requireView().findViewById(R.id.genderSpinner)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                genderSelected = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        val name = requireView().findViewById<EditText>(R.id.nameEdit)
        val bio = requireView().findViewById<EditText>(R.id.bioEdit)
        val update = requireView().findViewById<LinearLayout>(R.id.updateProfile)
        update.setOnClickListener { //on clicking update profile button, update all information
            val nameString = name.text.toString()
            val bioString = bio.text.toString()

            viewModel.updateProfile(nameString, genderSelected, bioString)
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