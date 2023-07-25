package com.example.packpals.views.pals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.packpals.R
import com.example.packpals.databinding.FragmentPalsBinding
import com.example.packpals.viewmodels.PalsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying incoming pal requests
 */
@AndroidEntryPoint
class PalsFragment : Fragment() {

    val viewModel: PalsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewBinding = FragmentPalsBinding.inflate(inflater, container, false)

        viewBinding.palRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PalsListAdapter()
        viewBinding.palRecyclerView.adapter = adapter

        viewModel.palsLiveData.observe(viewLifecycleOwner) { adapter.submitList(it) }

        val navController = this.findNavController()

        viewBinding.findAPalNavButton.setOnClickListener {
            navController.navigate(R.id.action_palsFragment_to_findAPalFragment)
        }

        viewBinding.incomingRequestsNavButton.setOnClickListener {
            navController.navigate(R.id.action_palsFragment_to_incomingPalRequestsFragment)
        }

        return viewBinding.root
    }
}