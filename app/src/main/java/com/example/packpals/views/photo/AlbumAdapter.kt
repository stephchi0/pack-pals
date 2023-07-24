package com.example.packpals.views.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.packpals.models.PhotoAlbum
import com.example.packpals.views.photo.GalleryAdapter
import com.example.packpals.views.photo.AlbumFragment
import android.widget.TextView
import com.example.packpals.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout

class AlbumAdapter(
    private var albums: List<PhotoAlbum>,
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var onAlbumItemClickListener: OnAlbumItemClickListener? = null
    fun setOnAlbumItemClickListener(listener: OnAlbumItemClickListener) {
        this.onAlbumItemClickListener = listener
    }

    interface OnAlbumItemClickListener {
        fun onAlbumItemClick(album: PhotoAlbum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(itemView)
    }
    fun updateAlbums(newAlbums: List<PhotoAlbum>) {
        albums = newAlbums
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]

        val galleryRecyclerView = holder.itemView.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        val galleryAdapter = GalleryAdapter(album.photos as List<String>)
        galleryRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 3)
        galleryRecyclerView.adapter = galleryAdapter

        holder.albumNameText.text = album.albumName
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumNameText: TextView = itemView.findViewById(R.id.nameAlbum)
        val button: LinearLayout = itemView.findViewById(R.id.addPicture)
        init {
            button.setOnClickListener {
                val position = adapterPosition
                Log.d("button press", "photo button is pressed")
                if (position != RecyclerView.NO_POSITION) {
                    val album = albums[position]
                    onAlbumItemClickListener?.onAlbumItemClick(album)
                }
            }
        }
    }
}