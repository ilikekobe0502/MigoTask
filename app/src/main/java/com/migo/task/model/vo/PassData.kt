package com.migo.task.model.vo

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.migo.task.model.enums.PassType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PassData(
    var passContent: Pass? = null,
    var passTitle: PassType? = null,
) : Parcelable