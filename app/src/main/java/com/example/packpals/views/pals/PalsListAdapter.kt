package com.example.packpals.views.pals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.packpals.databinding.ViewPalListItemBinding
import com.example.packpals.models.Pal

class PalsListAdapter : ListAdapter<Pal, PalsListAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Pal>() {
            override fun areItemsTheSame(oldPal: Pal, newPal: Pal) = oldPal.id == newPal.id

            override fun areContentsTheSame(oldPal: Pal, newPal: Pal) = oldPal == newPal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewPalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pal = getItem(position)
        holder.usernameView.text = pal.name

        Glide.with(holder.profilePicImageView)
            .load(pal.profilePictureURL)
            .into(holder.profilePicImageView)
    }

    inner class ViewHolder(binding: ViewPalListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val usernameView: TextView = binding.username
        val profilePicImageView: ImageView = binding.profilePic

        override fun toString(): String {
            return super.toString() + " '" + usernameView.text + "'"
        }
    }
}