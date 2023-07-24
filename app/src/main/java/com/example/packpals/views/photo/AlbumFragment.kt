package com.example.packpals.views.photo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.example.packpals.views.photo.AlbumAdapter
import android.view.LayoutInflater
import android.view.View
import com.example.packpals.models.PhotoAlbum
import android.view.ViewGroup
import android.widget.EditText
import com.example.packpals.R
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.packpals.viewmodels.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : Fragment(), AlbumAdapter.OnAlbumItemClickListener {
    private val viewModel: PhotoViewModel by viewModels()

    private val PERMISSION_REQUEST_CODE = 123
    private val GALLERY_PICK_REQUEST_CODE = 456

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_photo_album, container, false)

        val albumRecyclerView = view.findViewById<RecyclerView>(R.id.albumView)

        albumRecyclerView.layoutManager = GridLayoutManager(context, 1)

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            val albumAdapter = AlbumAdapter(albums as List<PhotoAlbum>)
            albumAdapter.setOnAlbumItemClickListener(this)
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
    override fun onAlbumItemClick(album: PhotoAlbum) {
        val selectedAlbumId = album.albumId

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            openGalleryPicker()
        }
    }
    private fun showCreateAlbumDialog(albumName: String) {
        viewModel.createAlbum(albumName)
    }
    private fun openGalleryPicker() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_PICK_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val albumName = view?.findViewById<EditText>(R.id.albumName)
        val albumId = albumName?.text.toString()
        if (requestCode == GALLERY_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                viewModel.addPhoto(albumId, imageUri, requireContext())
            }
        }
    }
}