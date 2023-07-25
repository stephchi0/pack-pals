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

class GalleryAdapter(private var photos: List<String>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val galleryImage: ImageView = itemView.findViewById(R.id.galleryPhoto)
    }
    fun updatePhotos(newPhotos: List<String>) {
        photos = newPhotos
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageUrl = photos[position]
        Glide.with(holder.itemView)
            .load(imageUrl)
            .dontTransform()
            .into(holder.galleryImage)
        Log.d("Glide", "Working")
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}