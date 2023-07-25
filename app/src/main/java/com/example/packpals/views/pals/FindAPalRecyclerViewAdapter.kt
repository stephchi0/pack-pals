package com.example.packpals.views.pals

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide

import com.example.packpals.views.pals.placeholder.PlaceholderContent.PlaceholderItem
import com.example.packpals.databinding.ViewFindAPalItemBinding
import com.example.packpals.models.Pal

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class FindAPalRecyclerViewAdapter(
    val onAddButtonClickListener: (Pal) -> Unit
) : ListAdapter<Pal, FindAPalRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Pal>() {
            override fun areItemsTheSame(oldPal: Pal, newPal: Pal) = oldPal.id == newPal.id

            override fun areContentsTheSame(oldPal: Pal, newPal: Pal) = oldPal == newPal
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
        val pal = getItem(position)
        holder.userNameView.text = pal?.name

        holder.addButton.setOnClickListener {
            pal?.let {
                onAddButtonClickListener(it)
            }
        }

        Glide.with(holder.profilePicImageView)
            .load(pal.profilePictureURL)
            .into(holder.profilePicImageView)
    }

    inner class ViewHolder(binding: ViewFindAPalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val userNameView: TextView = binding.userNameView
        val addButton: ImageButton = binding.addPalButton
        val profilePicImageView: ImageView = binding.previewImage

        override fun toString(): String {
            return super.toString() + " '" + userNameView.text + "'"
        }
    }

}