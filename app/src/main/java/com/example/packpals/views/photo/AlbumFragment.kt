package com.example.packpals.views.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.packpals.views.photo.AlbumAdapter
import android.view.LayoutInflater
import android.view.View
import com.example.packpals.models.PhotoAlbum
import android.view.ViewGroup
import android.widget.EditText
import com.example.packpals.R
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.packpals.viewmodels.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : Fragment() {
    private val viewModel: PhotoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_photo_album, container, false)

        val albumRecyclerView = view.findViewById<RecyclerView>(R.id.albumView)

        albumRecyclerView.layoutManager = GridLayoutManager(context, 1)

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            val albumAdapter = AlbumAdapter(albums as List<PhotoAlbum>)
            albumRecyclerView.adapter = albumAdapter
        }
        viewModel.fetchAlbums()

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createAlbumButton = view.findViewById<ImageView>(R.id.albumAdd)
        val albumName = view.findViewById<EditText>(R.id.albumName)
        createAlbumButton.setOnClickListener {
            val albumName = albumName.text.toString()
            showCreateAlbumDialog(albumName)
        }
    }
    private fun showCreateAlbumDialog(albumName: String) {
        viewModel.createAlbum(albumName)
    }
}