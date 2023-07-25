package com.example.packpals.views.map

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.packpals.R
import com.example.packpals.models.Itinerary_Item
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import java.net.URL


class InfoWindowAdapter(
    private val mContext: Context,
    private val markerMap: HashMap<Marker, Itinerary_Item>
) : GoogleMap.InfoWindowAdapter {
    var mWindow: View = LayoutInflater.from(mContext).inflate(R.layout.marker_info_window, null)
    private fun setInfoWindowValues(marker: Marker) {
        val itineraryItem = markerMap.get(marker)
        val windowTitle = mWindow.findViewById<TextView>(R.id.location_title)
        windowTitle.text = marker.title
        val windowSnippet = mWindow.findViewById<TextView>(R.id.location_snippet)
        windowSnippet.text = marker.snippet
        val windowImage = mWindow.findViewById<ImageView>(R.id.location_image)
        if (itineraryItem != null) {
            Glide
                .with(mContext)
                .load(itineraryItem.photo_reference)
                .into(windowImage)
        } else {
            windowImage.setImageResource(R.drawable.ic_itinerary)
        }
    }

    override fun getInfoContents(p0: Marker): View? {
        setInfoWindowValues(p0)
        return mWindow
    }

    override fun getInfoWindow(p0: Marker): View? {
        setInfoWindowValues(p0)
        return mWindow
    }


}