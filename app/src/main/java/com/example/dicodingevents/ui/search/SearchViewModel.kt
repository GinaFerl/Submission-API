package com.example.dicodingevents.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.response.ListEventResponse
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val apiService: ApiService) : ViewModel() {
    private val _search = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _search

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    companion object {
        private const val TAG = "SearchViewModel"
    }

    fun getSearch(query: String) {
        _isLoading.value = true
        apiService.getSearch(query).enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _search.value = response.body()?.listEvents ?: emptyList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ListEventResponse>, t: Throwable) {
                _isLoading.value = false
                _search.value = emptyList()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getAllEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllevents()
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(
                        "FinishedFragment",
                        "Number of events: ${response.body()?.listEvents?.size}"
                    )
                    _search.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ListEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}