package com.example.packpals.views.pals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.packpals.R
import com.example.packpals.viewmodels.PalsViewModel
import kotlinx.android.synthetic.main.fragment_find_a_pal.view.findAPalRecyclerView
import kotlinx.android.synthetic.main.fragment_find_a_pal.view.findAPalSubmitButton
import kotlinx.android.synthetic.main.fragment_find_a_pal.view.findAPalUsernameInput

/**
 * Fragment for the Find A Pal screen
 */
class FindAPalFragment : Fragment() {
    private val viewModel: PalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find_a_pal, container, false)

        val recyclerView = view.findAPalRecyclerView
        recyclerView.adapter = FindAPalRecyclerViewAdapter(viewModel.palRequestQueryResultLiveData, this)
        recyclerView.layoutManager = LinearLayoutManager(context)

        view.findAPalSubmitButton.setOnClickListener {
            val query = view.findAPalUsernameInput.text.toString()
            viewModel.addPal(query)
        }

        return view
    }
}