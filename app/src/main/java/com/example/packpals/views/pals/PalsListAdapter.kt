package com.example.packpals.views.pals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.packpals.databinding.ViewPalRequestItemBinding
import com.example.packpals.models.Pal
import com.example.packpals.models.PalRequest

class PalsListAdapter(val palsLiveData: LiveData<List<Pal>>, lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<PalsListAdapter.ViewHolder>() {

    init {
        palsLiveData.observe(lifecycleOwner) {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewPalRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = palsLiveData.value?.get(position)
        holder.usernameView.text = item?.name
    }

    override fun getItemCount(): Int {
        return palsLiveData.value?.size ?: 0
    }

    inner class ViewHolder(binding: ViewPalRequestItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val usernameView: TextView = binding.username

        override fun toString(): String {
            return super.toString() + " '" + usernameView.text + "'"
        }
    }
}