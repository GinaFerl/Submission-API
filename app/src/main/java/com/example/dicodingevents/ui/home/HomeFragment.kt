package com.example.dicodingevents.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.databinding.FragmentHomeBinding
import com.example.dicodingevents.ui.detail.DetailActivity
import com.example.dicodingevents.ui.search.SearchActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var adapter: HomeUpcomingAdapter
    private lateinit var adapterFinished: HomeFinishedAdapter
    private lateinit var viewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        adapter = HomeUpcomingAdapter{
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", it.id)
            startActivity(intent)
        }

        adapterFinished = HomeFinishedAdapter{
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", it.id)
            startActivity(intent)
        }

        binding.rvHomeUpcoming.adapter = adapter
        binding.rvHomeUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeUpcoming.setHasFixedSize(true)

        binding.rvHomeFinished.adapter = adapterFinished
        binding.rvHomeFinished.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            adapterFinished.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        if (isNetworkAvailable(requireContext())) {
            showLoading(true)
            viewModel.getHomeUpcomingEvents()
            viewModel.getHomeFinishedEvents()
        } else {
            showLoading(false)
            binding.noInternet.visibility = View.VISIBLE
        }

        binding.btn1.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}