package com.migo.task.model.api.model.response


import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
    @ColumnInfo
    @SerializedName("bs")
    val bs: String,
    @ColumnInfo
    @SerializedName("catchPhrase")
    val catchPhrase: String,
    @ColumnInfo
    @SerializedName("name")
    val name: String
): Parcelable