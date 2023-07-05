package com.example.packpals.views.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.LoginPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val viewModel: LoginPageViewModel by viewModels()
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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uploadProfilePictureButton = requireView().findViewById<Button>(R.id.uploadProfilePictureButton)
        uploadProfilePictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImage.launch(intent)
        }

        val registerButton = requireView().findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val txtName = requireView().findViewById<EditText>(R.id.editTextRegisterName).text.toString()
            val txtEmail = requireView().findViewById<EditText>(R.id.editTextRegisterEmailAddress).text.toString()
            val txtPassword = requireView().findViewById<EditText>(R.id.editTextRegisterPassword).text.toString()

            viewModel.register(txtName, txtEmail, txtPassword)
        }

        viewModel.registrationSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.loginFragmentContainerView, LoginFragment())
                transaction.commit()
                viewModel.reset()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {

            }
    }
}