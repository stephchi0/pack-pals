package com.example.packpals.views.packinglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.PackingListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PackingListFragment : Fragment() {
    private val viewModel: PackingListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_packing_list, container, false)
    }
}