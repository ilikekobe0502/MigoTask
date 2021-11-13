package com.migo.task.model.db.dao

import androidx.room.*
import com.migo.task.model.enums.PassType
import com.migo.task.model.vo.Pass
import kotlinx.coroutines.flow.Flow

@Dao
interface PassDao {
    @Query("SELECT * FROM 'pass' WHERE type = :passType")
    fun getPass(passType: PassType): Flow<List<Pass>>

    @Update
    fun updateItem(data: Pass)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(data: Pass)
}