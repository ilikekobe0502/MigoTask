package com.migo.task.model.api

import com.google.gson.Gson
import com.migo.task.API_HOST_PRIVATE_URL
import com.migo.task.API_HOST_PUBLIC_URL
import com.migo.task.model.api.model.response.ApiStatus
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiRepository @Inject constructor(private val okHttpClient: OkHttpClient) {

    private val wifiApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .baseUrl(API_HOST_PRIVATE_URL)
            .build()
            .create(ApiService::class.java)
    }
    private val cellNetworkApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .baseUrl(API_HOST_PUBLIC_URL)
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getStatus(isWifi: Boolean): Response<ApiStatus> {
        val apiService = if (isWifi) wifiApiService else cellNetworkApiService
        return apiService.getStatus()
    }
}