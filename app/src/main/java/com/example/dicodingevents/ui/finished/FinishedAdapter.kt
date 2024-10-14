package com.example.dicodingevents.ui.finished

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.databinding.FinishedItemBinding

class FinishedAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, FinishedAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: FinishedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvName.text = event.name
            binding.locationValue.text = event.cityName
            binding.quotaValue.text = event.quota.toString()
            binding.registrantValue.text = event.registrants.toString()
            Glide.with(binding.ivFinished.context)
                .load(event.imageLogo)
                .into(binding.ivFinished)

            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FinishedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}