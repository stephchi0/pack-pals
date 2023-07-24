package com.example.packpals.views.pals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.packpals.databinding.FragmentIncomingPalRequestsBinding
import com.example.packpals.viewmodels.IncomingPalRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying incoming pal requests
 */
@AndroidEntryPoint
class IncomingPalRequestsFragment : Fragment() {

    val viewModel: IncomingPalRequestsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewBinding = FragmentIncomingPalRequestsBinding.inflate(inflater, container, false)

        viewBinding.palRequestRecyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.palRequestRecyclerView.adapter =
            IncomingPalRequestsAdapter(viewModel.palRequestsLiveData, this) { request ->
            viewModel.acceptPalRequest(request.id)
        }

        return viewBinding.root
    }
}