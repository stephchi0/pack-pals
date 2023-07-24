package com.example.packpals.views.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.packpals.models.PhotoAlbum
import android.widget.ImageView
import android.widget.TextView
import com.example.packpals.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.util.Log
import com.example.packpals.viewmodels.PhotoViewModel

class GalleryAdapter(private val photos: List<String>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val galleryImage: ImageView = itemView.findViewById(R.id.galleryPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageUrl = photos[position]

        // Load the image using Glide or your preferred image loading library
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.galleryImage)
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}