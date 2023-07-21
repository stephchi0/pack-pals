package com.example.packpals.views.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.packpals.models.PhotoAlbum
import android.view.ViewGroup
import com.example.packpals.R
import com.example.packpals.views.photo.AlbumAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_photo_album, container, false)

        val albumRecyclerView = view.findViewById<RecyclerView>(R.id.albumView)


        val albums = listOf(
            PhotoAlbum("Album 1", "CN tower", R.mipmap.cn_tower, emptyList()),
            PhotoAlbum("Album 2", "CN tower again again", R.mipmap.cn_tower, emptyList()),
            PhotoAlbum("Album 3", "CN tower again", R.mipmap.cn_tower, emptyList())
        )

        val albumAdapter = AlbumAdapter(albums)

        albumRecyclerView.layoutManager = GridLayoutManager(context, 2)
        albumRecyclerView.adapter = albumAdapter

        return view
    }

    //implement on-click to gallery
}