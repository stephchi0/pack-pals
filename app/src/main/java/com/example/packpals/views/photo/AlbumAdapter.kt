package com.example.packpals.views.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.packpals.models.PhotoAlbum
import android.widget.TextView
import com.example.packpals.R
import androidx.recyclerview.widget.RecyclerView
import android.util.Log


class AlbumAdapter(private val albums: List<PhotoAlbum>) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumNameText: TextView = itemView.findViewById(R.id.nameAlbum)
    }

    interface OnAlbumClickListener {
        fun onAlbumClick(album: PhotoAlbum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.albumNameText.text = album.title
    }

    override fun getItemCount(): Int {
        Log.d("AlbumFragment", "Albums list size: ${albums.size}")
        return albums.size
    }
}