package com.example.dicodingevents.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.databinding.EventsItemBinding

class SearchAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, SearchAdapter.ViewHolder>(DIFF_CALLBACK)  {

    class ViewHolder (private val binding: EventsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvName.text = event.name
            binding.locationValue.text = event.cityName
            binding.quotaValue.text = event.quota.toString()
            binding.registrantValue.text = event.registrants.toString()
            Glide.with(binding.ivSearch.context)
                .load(event.imageLogo)
                .into(binding.ivSearch)

            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    private var filteredEventsList : List<ListEventsItem> = emptyList()

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            currentList // `currentList` is provided by ListAdapter
        } else {
            currentList.filter { event ->
                event.name.contains(query, ignoreCase = true)
            }
        }
        submitList(filteredList)
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
        val binding = EventsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = filteredEventsList[position]
        holder.bind(event, onItemClick)
    }
}