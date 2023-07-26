package com.example.packpals.views.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.packpals.R
import com.example.packpals.models.PhotoAlbum
import com.example.packpals.viewmodels.PhotoViewModel
import com.example.packpals.views.photo.AlbumAdapter
import com.example.packpals.views.photo.GalleryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetails : Fragment(), AlbumAdapter.OnAlbumItemClickListener {

    private val viewModel: PhotoViewModel by viewModels()
    private var selectedAlbumId: String? = null
    private val GALLERY_PICK_REQUEST_CODE = 456
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationStartDate = view.findViewById<TextView>(R.id.locationStartDate)
        locationStartDate.text = "Start Date: " + arguments?.getString("locationStartDate")
        val locationEndDate = view.findViewById<TextView>(R.id.locationEndDate)
        locationEndDate.text = "End Date: " + arguments?.getString("locationEndDate")
        val locationForecast = view.findViewById<TextView>(R.id.locationForecast)
        locationForecast.text = "Forecast: " + arguments?.getString("locationForecast")
        val locationDetailsImage = view.findViewById<ImageView>(R.id.locationDetailsImage)
        Glide
            .with(requireContext())
            .load(arguments?.getString("locationPhoto"))
            .into(locationDetailsImage)

        val albumRecyclerView = view.findViewById<RecyclerView>(R.id.locationDetailAlbumView)
        val albumAdapter = AlbumAdapter(emptyList())
        val galleryAdapter = GalleryAdapter(emptyList())

        albumRecyclerView.layoutManager = GridLayoutManager(context, 1)
        albumRecyclerView.adapter = albumAdapter

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            for (album in albums) {
                if (album?.albumName == arguments?.getString("locationTitle")) {
                    albumAdapter.updateAlbums(listOf(album!!))
                    albumAdapter.setOnAlbumItemClickListener(this)
                    galleryAdapter.updatePhotos(album.photos as List<String>)
                }
            }
        }

        viewModel.fetchAlbums()
    }

    override fun onDestroy() {
        setFragmentResult("requestKey", bundleOf("camera_position" to
                arguments?.getParcelable("camera_position")))
        super.onDestroy()
    }

    override fun onAlbumItemClick(album: PhotoAlbum) {
        selectedAlbumId = album.albumId
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            openGalleryPicker()
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                GALLERY_PICK_REQUEST_CODE

            )
        } else {
            openGalleryPicker()
        }
    }
    private fun openGalleryPicker() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_PICK_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val albumUpdated = selectedAlbumId
        if (requestCode == GALLERY_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                if (albumUpdated != null) {
                    viewModel.addPhoto(albumUpdated, imageUri, requireContext())
                }
            }
        }
    }
}