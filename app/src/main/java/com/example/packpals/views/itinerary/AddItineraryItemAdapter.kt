package com.example.packpals.views.itinerary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.packpals.R
import com.example.packpals.models.Itinerary_Item
import com.example.packpals.viewmodels.ItineraryPageViewModel
import kotlinx.android.synthetic.main.view_itenerary_item.view.image
import kotlinx.android.synthetic.main.view_itenerary_item.view.tvdate
import kotlinx.android.synthetic.main.view_itenerary_item.view.tvforecast
import kotlinx.android.synthetic.main.view_itenerary_item.view.tvlocation

class AddItineraryItemAdapter(
    private var reccItems: MutableList<Itinerary_Item>,
    private var items: MutableList<Itinerary_Item>
) : RecyclerView.Adapter<AddItineraryItemAdapter.ItemViewHolder>(){

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

    fun addItem(item:Itinerary_Item){
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun updateLists(newList1: MutableList<Itinerary_Item>, newList2: MutableList<Itinerary_Item>) {
        reccItems = newList1
        items = newList2
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val curItem = reccItems[position]

        holder.itemView.apply{
            tvlocation.text = curItem.location
            tvdate.text = curItem.date
            tvforecast.text = curItem.forecast
            image.setImageResource(R.mipmap.fenugs)

            setOnClickListener {
                addItem(curItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return reccItems.size
    }
}