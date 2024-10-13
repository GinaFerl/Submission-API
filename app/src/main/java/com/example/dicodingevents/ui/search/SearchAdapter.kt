package com.example.dicodingevents.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.databinding.EventsItemBinding

class SearchAdapter(private val onItemClick: (ListEventsItem) -> Unit) : RecyclerView.Adapter<SearchAdapter.ViewHolder>()  {
    inner class ViewHolder (private val binding: EventsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvName.text = event.name
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

    private var eventsList : List<ListEventsItem> = emptyList()
    private var filteredEventsList : MutableList<ListEventsItem> = mutableListOf()

    fun setData(event: List<ListEventsItem>) {
        eventsList = event
        filteredEventsList.clear()
        filteredEventsList.addAll(eventsList)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredEventsList.clear()
        if (query.isEmpty()) {
            filteredEventsList.addAll(eventsList)
        } else {
            val filteredList = eventsList.filter { event ->
                event.name.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val binding = EventsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bind(filteredEventsList[position])
    }

    override fun getItemCount(): Int = filteredEventsList.size
}