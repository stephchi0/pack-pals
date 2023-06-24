package com.example.packpals.views.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.packpals.R
import com.example.packpals.activities.MapsFragment
import com.example.packpals.activities.NavigationDrawerViewActivity
import com.example.packpals.viewmodels.LoginPageViewModel
import com.example.packpals.views.expenses.ExpenseListFragment

class LoginFragment : Fragment() {
    private val viewModel: LoginPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerTextView = requireView().findViewById<TextView>(R.id.registerTextView)
        registerTextView.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.loginFragmentContainerView, RegisterFragment())
            transaction.commit()
        }

        val loginButton = requireView().findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val txtEmail = requireView().findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
            val txtPassword = requireView().findViewById<EditText>(R.id.editTextPassword).text.toString()
            viewModel.login(txtEmail, txtPassword)
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                val intent = Intent(requireActivity(), NavigationDrawerViewActivity::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {

            }
    }
}