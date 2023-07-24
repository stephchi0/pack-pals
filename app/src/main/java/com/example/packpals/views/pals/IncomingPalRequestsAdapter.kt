package com.example.packpals.views.pals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.packpals.databinding.ViewPalRequestItemBinding
import com.example.packpals.models.PalRequest

class IncomingPalRequestsAdapter(
    private val requestsLiveData: LiveData<List<PalRequest>>,
    lifecycleOwner: LifecycleOwner,
    acceptButtonListener: (PalRequest) -> Unit
) : RecyclerView.Adapter<IncomingPalRequestsAdapter.ViewHolder>() {

    init {
        requestsLiveData.observe(lifecycleOwner) {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewPalRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = requestsLiveData.value?.get(position)
        holder.usernameView.text = item?.name
        holder.acceptButton.setOnClickListener {  }
    }

    override fun getItemCount(): Int {
        return requestsLiveData.value?.size ?: 0
    }

    inner class ViewHolder(binding: ViewPalRequestItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val usernameView: TextView = binding.username
        val acceptButton = binding.acceptButton

        override fun toString(): String {
            return super.toString() + " '" + usernameView.text + "'"
        }
    }
}