package com.example.packpals.views.pals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.packpals.R
import kotlinx.android.synthetic.main.fragment_find_a_pal.view.findAPalSubmitButton
import kotlinx.android.synthetic.main.fragment_find_a_pal.view.findAPalUsernameInput

/**
 * Fragment for the Find A Pal screen
 */
class FindAPalFragment : Fragment() {

//    private lateinit var recyclerView: RecyclerView
    private val viewModel: FindAPalViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find_a_pal, container, false)

        view.findAPalSubmitButton.setOnClickListener {
            val query = view.findAPalUsernameInput.text.toString()
            viewModel.addPal(query)
        }

        return view
    }
}