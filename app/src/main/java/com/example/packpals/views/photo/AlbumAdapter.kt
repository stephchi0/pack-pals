package com.example.packpals.views.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.packpals.models.PhotoAlbum
import android.widget.TextView
import com.example.packpals.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log

class AlbumAdapter(
    private val albums: List<PhotoAlbum>,

) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumNameText: TextView = itemView.findViewById(R.id.nameAlbum)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]

        val galleryRecyclerView = holder.itemView.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        val galleryAdapter = GalleryAdapter(album.photos as List<String>)
        galleryRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 3)
        galleryRecyclerView.adapter = galleryAdapter

        holder.albumNameText.text = album.albumId
    }

    override fun getItemCount(): Int {
        return albums.size
    }
}