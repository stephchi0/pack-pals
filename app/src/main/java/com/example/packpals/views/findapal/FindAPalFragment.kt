package com.example.packpals.views.findapal

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.packpals.R
import com.example.packpals.views.findapal.placeholder.PlaceholderContent
import kotlinx.android.synthetic.main.fragment_find_a_pal.view.findAPalRecyclerView

/**
 * A fragment representing a list of Items.
 */
class FindAPalFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find_a_pal, container, false)

        // Set the adapter
        recyclerView = view.findAPalRecyclerView
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FindAPalRecyclerViewAdapter(PlaceholderContent.ITEMS)
        }
        return view
    }
}