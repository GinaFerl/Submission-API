package com.example.dicodingevents.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.response.Event
import com.example.dicodingevents.data.response.ListEventResponse
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.ui.finished.FinishedViewModel
import com.example.dicodingevents.ui.finished.FinishedViewModel.Companion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewModel: ViewModel() {
    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "EventsViewModel"
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
                    _events.value = response.body()?.listEvents
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