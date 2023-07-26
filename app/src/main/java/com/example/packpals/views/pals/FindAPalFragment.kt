package com.example.packpals.views.pals

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.packpals.R
import com.example.packpals.viewmodels.FindAPalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_find_a_pal.view.findAPalRecyclerView
import kotlinx.android.synthetic.main.fragment_find_a_pal.view.searchInput

/**
 * Fragment for the Find A Pal screen
 */
@AndroidEntryPoint
class FindAPalFragment : Fragment() {
    private val viewModel: FindAPalViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find_a_pal, container, false)

        val recyclerView = view.findAPalRecyclerView
        val adapter = FindAPalRecyclerViewAdapter { pal ->
            pal.id?.let {
                viewModel.sendPalRequest(it) { success ->
                    val text = if (success) "Request sent" else "Request failed"
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                }
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val queryResultLiveData = viewModel.palRequestQueryResultLiveData
        queryResultLiveData.observe(viewLifecycleOwner) { adapter.submitList(it) }

        view.searchInput.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    viewModel.queryPals(view.searchInput.text.toString())
                    view.searchInput.clearFocus()
                    val inputManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(view.windowToken, 0)
                    true
                }
                else -> false
            }

        }

        return view
    }
}