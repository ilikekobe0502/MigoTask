package com.migo.task.model.api.model.response


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey
    @SerializedName("id")
    var id: Int,
    @Embedded(prefix = "company_")
    @SerializedName("company")
    var company: Company,
    @ColumnInfo
    @SerializedName("email")
    var email: String,
    @ColumnInfo
    @SerializedName("name")
    var name: String,
    @ColumnInfo
    @SerializedName("pictureUrl")
    var pictureUrl: String,
    @ColumnInfo
    @SerializedName("username")
    var username: String,

    @ColumnInfo
    var favorite: Boolean = false
): Parcelable