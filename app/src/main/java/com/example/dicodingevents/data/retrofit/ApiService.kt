package com.example.dicodingevents.data.retrofit

import com.example.dicodingevents.data.response.DetailResponse
import com.example.dicodingevents.data.response.ListEventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<DetailResponse>

    @GET("/events")
    fun getUpcoming(
        @Query("active") active: Int = 1,
    ): Call<ListEventResponse>

    @GET("/events")
    fun getFinished(
        @Query("active") active: Int = 0,
    ): Call<ListEventResponse>

    @GET("/events")
    fun getSearch(
        @Query("q") q: String,
    ): Call<ListEventResponse>

    @GET("/events")
    fun getFinished5(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int = 5,
    ): Call<ListEventResponse>

    @GET("/events")
    fun getUpcoming5(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 5,
    ): Call<ListEventResponse>

    @GET("/events")
    fun getAllevents(): Call<ListEventResponse>
}