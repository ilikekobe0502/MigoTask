package com.migo.task.model.api

import com.migo.task.model.api.model.response.*
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<Contacts>
}