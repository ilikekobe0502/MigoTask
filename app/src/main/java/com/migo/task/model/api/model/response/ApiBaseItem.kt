package com.migo.task.model.api.model.response

import com.google.gson.annotations.SerializedName

data class ApiBaseItem<T>(
    @SerializedName("result")
    val result: T?
)