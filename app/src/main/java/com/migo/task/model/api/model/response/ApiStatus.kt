package com.migo.task.model.api.model.response

import com.google.gson.annotations.SerializedName

data class ApiStatus(
    @SerializedName("status")
    val result: Int,
    @SerializedName("message")
    val message: String
)