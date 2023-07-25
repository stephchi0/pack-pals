package com.example.packpals.views.pals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.packpals.databinding.FragmentIncomingPalRequestsBinding
import com.example.packpals.models.PalRequest
import com.example.packpals.viewmodels.IncomingPalRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying incoming pal requests
 */
@AndroidEntryPoint
class IncomingPalRequestsFragment : Fragment() {

    val viewModel: IncomingPalRequestsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewBinding = FragmentIncomingPalRequestsBinding.inflate(inflater, container, false)

        val requestItemListener = object : IncomingPalRequestsAdapter.ButtonListener {
            override fun onAcceptButtonPressed(request: PalRequest) {
                request.id?.let { viewModel.acceptPalRequest(it) }
            }

            override fun onDeclineButtonPressed(request: PalRequest) {
                request.id?.let { viewModel.declinePalRequest(it) }
            }
        }
        viewBinding.palRequestRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = IncomingPalRequestsAdapter(requestItemListener)
        viewBinding.palRequestRecyclerView.adapter = adapter

        viewModel.palRequestsLiveData.observe(viewLifecycleOwner) { adapter.submitList(it) }

        return viewBinding.root
    }
}