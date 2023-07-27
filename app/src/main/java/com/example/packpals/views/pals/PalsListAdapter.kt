package com.example.packpals.views.pals

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.packpals.R
import com.example.packpals.databinding.ViewPalListItemBinding
import com.example.packpals.models.Pal

class PalsListAdapter(
    val context: Context?,
    private val onPopupMenuItemClickListener: (MenuItem, Pal) -> Boolean
) : ListAdapter<Pal, PalsListAdapter.ViewHolder>(DIFF_CALLBACK) {
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

        pal.profilePictureURL?.let {
            Glide.with(holder.profilePicImageView)
                .load(it)
                .into(holder.profilePicImageView)
        }

        val popupMenu = PopupMenu(context, holder.menuButton)
        popupMenu.menuInflater.inflate(R.menu.pals_list_item_menu, popupMenu.menu)
        val listener = { item: MenuItem -> onPopupMenuItemClickListener(item, pal) }
        popupMenu.setOnMenuItemClickListener(listener)

        holder.menuButton.setOnClickListener { popupMenu.show() }
    }

    inner class ViewHolder(binding: ViewPalListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val usernameView: TextView = binding.username
        val profilePicImageView: ImageView = binding.profilePic
        val menuButton: ImageButton = binding.tripTripleDotMenu

        override fun toString(): String {
            return super.toString() + " '" + usernameView.text + "'"
        }
    }
}