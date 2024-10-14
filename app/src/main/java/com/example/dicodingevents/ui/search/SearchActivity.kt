package com.example.dicodingevents.ui.search

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.databinding.ActivitySearchBinding
import com.example.dicodingevents.ui.detail.DetailActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiService()
        searchViewModel = ViewModelProvider(this, SearchViewModelFactory(apiService))[SearchViewModel::class.java]
        eventsViewModel = ViewModelProvider(this)[EventsViewModel::class.java]

        setupRecyclerView()
        observeViewModels()
        setupSearchView()

        if (isNetworkAvailable(this)) {
            eventsViewModel.getAllEvents()
        } else {
            showLoading(false)
            binding.noInternet.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter { event ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("EVENT_ID", event.id)
            }
            startActivity(intent)
        }

        eventsAdapter = EventsAdapter { event ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("EVENT_ID", event.id)
            }
            startActivity(intent)
        }

        binding.rvEvents.apply {
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }

        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
    }

    private fun observeViewModels() {
        // Observe events data from searchViewModel
        searchViewModel.events.observe(this) { events ->
            searchAdapter.submitList(events)
        }

        searchViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        eventsViewModel.events.observe(this) { events ->
            eventsAdapter.submitList(events)
        }

        eventsViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString()
                if (query.isNotEmpty()) {
                    searchBar.setText(query)
                    searchView.hide()
                    searchViewModel.getSearch(query) // Fetch filtered events from ViewModel
                }
                true
            }

            searchView.editText.doOnTextChanged { text, _, _, _ ->
                val query = text.toString()
                searchAdapter.filter(query)
            }
        }
    }

    private fun showLoading(isLoading: Boolean?) {
        binding.pbSearch.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}