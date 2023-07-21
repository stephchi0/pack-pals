package com.example.packpals.views.pals

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

import com.example.packpals.views.pals.placeholder.PlaceholderContent.PlaceholderItem
import com.example.packpals.databinding.ViewFindAPalItemBinding
import com.example.packpals.models.Pal
import com.example.packpals.viewmodels.FindAPalViewModel
import com.example.packpals.viewmodels.PalsFragmentViewModel

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class FindAPalRecyclerViewAdapter(
    private val palsLiveData: LiveData<List<Pal>>,
    lifecycleOwner: LifecycleOwner,
    val onAddButtonClickListener: (Pal) -> Unit
) : RecyclerView.Adapter<FindAPalRecyclerViewAdapter.ViewHolder>() {

    init {
        palsLiveData.observe(lifecycleOwner) {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewFindAPalItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pal = palsLiveData.value?.get(position)
        holder.userNameView.text = pal?.name
        holder.addButton.setOnClickListener {
            pal?.let {
                onAddButtonClickListener(it)
            }
        }
    }

    override fun getItemCount(): Int = palsLiveData.value?.size ?: 0

    inner class ViewHolder(binding: ViewFindAPalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val userNameView: TextView = binding.userNameView
        val addButton: ImageButton = binding.addPalButton

        override fun toString(): String {
            return super.toString() + " '" + userNameView.text + "'"
        }
    }

}