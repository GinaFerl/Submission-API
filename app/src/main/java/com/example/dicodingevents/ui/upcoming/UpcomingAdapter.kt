package com.example.dicodingevents.ui.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.databinding.UpcomingItemBinding

class UpcomingAdapter(private val onItemClick: (ListEventsItem) -> Unit): ListAdapter<ListEventsItem, UpcomingAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: UpcomingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvName.text = event.name
            binding.quotaValue.text = event.quota.toString()
            binding.registrantValue.text = event.registrants.toString()
            Glide.with(binding.ivUpcoming.context)
                .load(event.mediaCover)
                .into(binding.ivUpcoming)

            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UpcomingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }
}