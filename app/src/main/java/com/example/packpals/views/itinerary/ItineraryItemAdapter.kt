package com.example.packpals.views.itinerary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.packpals.R
import com.example.packpals.models.Itinerary_Item
import kotlinx.android.synthetic.main.view_itenerary_item.view.image
import kotlinx.android.synthetic.main.view_itenerary_item.view.tvdate
import kotlinx.android.synthetic.main.view_itenerary_item.view.tvforecast
import kotlinx.android.synthetic.main.view_itenerary_item.view.tvlocation

class ItineraryItemAdapter (
    private val items: List<Itinerary_Item>
    ) : RecyclerView.Adapter<ItineraryItemAdapter.ItemViewHolder>(){

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_itenerary_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val curItem = items[position]
        holder.itemView.apply{
            tvlocation.text = curItem.location
            tvdate.text = curItem.date
            tvforecast.text = curItem.forecast
            image.setImageResource(R.mipmap.fenugs)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}