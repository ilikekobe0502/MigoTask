package com.migo.task.model.vo

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.migo.task.model.enums.PassType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "pass")
data class Pass(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var type: PassType,
    var timeValue: Int,
    var insertionDate: Long = Date().time,
    var activationDate: Long = -1,
    var expiredDate: Long = -1,
    var price: Int = 0,
    var activate: Boolean = false,
) : Parcelable