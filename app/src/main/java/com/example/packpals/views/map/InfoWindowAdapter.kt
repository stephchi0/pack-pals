package com.example.packpals.views.map

import com.example.packpals.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class InfoWindowAdapter (private val mContext: Context) : GoogleMap.InfoWindowAdapter {
    var mWindow: View = LayoutInflater.from(mContext).inflate(R.layout.marker_info_window, null)

    private fun setInfoWindowValues(marker: Marker) {
        val windowTitle = mWindow.findViewById<TextView>(R.id.location_title)
        windowTitle.text = marker.title
        val windowSnippet = mWindow.findViewById<TextView>(R.id.location_snippet)
        windowSnippet.text = marker.snippet
        val windowImage = mWindow.findViewById<ImageView>(R.id.location_image)
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