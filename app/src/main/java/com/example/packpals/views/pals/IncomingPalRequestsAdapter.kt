package com.example.packpals.views.pals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.packpals.databinding.ViewPalRequestItemBinding
import com.example.packpals.models.Pal
import com.example.packpals.models.PalRequest

class IncomingPalRequestsAdapter(
    private val buttonListener: ButtonListener
) : ListAdapter<PalRequest, IncomingPalRequestsAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PalRequest>() {
            override fun areItemsTheSame(oldRequest: PalRequest, newRequest: PalRequest) = oldRequest.id == newRequest.id

            override fun areContentsTheSame(oldRequest: PalRequest, newRequest: PalRequest) = oldRequest == newRequest
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewPalRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = getItem(position)
        holder.usernameView.text = request?.name
        holder.acceptButton.setOnClickListener {
            request?.let(buttonListener::onAcceptButtonPressed)
        }
        holder.declineButton.setOnClickListener {
            request?.let(buttonListener::onDeclineButtonPressed)
        }
    }

    inner class ViewHolder(binding: ViewPalRequestItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val usernameView: TextView = binding.username
        val acceptButton = binding.acceptButton
        val declineButton = binding.declineButton

        override fun toString(): String {
            return super.toString() + " '" + usernameView.text + "'"
        }
    }

    interface ButtonListener {
        fun onAcceptButtonPressed(request: PalRequest)

        fun onDeclineButtonPressed(request: PalRequest)
    }
}