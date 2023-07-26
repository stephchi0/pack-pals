package com.example.packpals.views.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.models.Itinerary_Item
import com.example.packpals.viewmodels.ItineraryPageViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient


class MapsFragment : Fragment(), GoogleMap.OnInfoWindowClickListener {
    private var map: GoogleMap? = null
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private var cameraPosition: CameraPosition? = null
    private var locationPermissionGranted = false
    private val sydney = LatLng(-34.0, 151.0)
    private val viewModel: ItineraryPageViewModel by activityViewModels()
    private var markerMap: HashMap<Marker, Itinerary_Item> = HashMap<Marker, Itinerary_Item>()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         */
        this.map = googleMap
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()
        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
        viewModel.itineraryItemsList.observe(viewLifecycleOwner) { itineraryItems ->
            plotItineraryMarkers(itineraryItems)
            googleMap.setInfoWindowAdapter(InfoWindowAdapter(requireContext(),markerMap))
        }

        googleMap.setOnInfoWindowClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.itineraryItemsList.observe(viewLifecycleOwner) {
            Log.i("MapPageActivity", it?.fold("Location IDs:") { acc, cur -> "$acc ${cur.location}" } ?: "[ERROR]")
        }
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            cameraPosition = bundle.getParcelable(KEY_CAMERA_POSITION)
        }
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        Log.i("MapPageActivity", getString(R.string.MAPS_API_KEY))
        Places.initialize(requireActivity().getApplicationContext(), getString(R.string.MAPS_API_KEY))
        placesClient = Places.createClient(requireContext())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) { PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)  == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            getLocationPermission()
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                Log.i("MapPageActivity", "Testing1")
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (cameraPosition != null ) {
                            map?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition!!))
                        } else if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(sydney, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun plotItineraryMarkers(itineraryLocations : List<Itinerary_Item>) {
        markerMap.clear()
        map?.clear()
        for (itineraryItem in itineraryLocations) {
            val conf = Bitmap.Config.ARGB_8888
            val bmp = Bitmap.createBitmap(300, 150, conf)
            val canvas = Canvas(bmp)
            val font = Paint()
            font.textSize = 40F
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_marker),110F,50F,null)
            itineraryItem.location?.let {
                canvas.drawText(
                    it,
                    0F,
                    30F,
                    font
                )
            }
            val marker = map?.addMarker(
            MarkerOptions()
                .position(LatLng(itineraryItem.geopoint!!.latitude,itineraryItem.geopoint!!.longitude))
                .title(itineraryItem.location)
                .snippet("Forecast: " + itineraryItem.forecast)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                .anchor(0.5F, 1F)
            )
            if (marker != null) {
                itineraryItem.itemId?.let { markerMap.put(marker, itineraryItem) }
            }
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        Log.i("MapPageActivity", "Info window clicked")
        val itineraryItem = markerMap.get(marker)
        val bundle = Bundle()
        if (itineraryItem != null) {
            bundle.putString("locationTitle", itineraryItem.location)
            bundle.putString("locationForecast", itineraryItem.forecast)
            bundle.putString("locationStartDate", itineraryItem.startDate.toString())
            bundle.putString("locationEndDate", itineraryItem.endDate.toString())
            bundle.putString("locationPhoto", itineraryItem.photo_reference)
            bundle.putParcelable(KEY_CAMERA_POSITION, map?.cameraPosition)
        }
        findNavController().navigate(R.id.locationDetailsFragment,bundle)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        private val TAG = "MAPPING"
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}