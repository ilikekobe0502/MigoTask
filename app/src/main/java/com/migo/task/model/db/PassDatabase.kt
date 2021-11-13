package com.migo.task.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.migo.task.model.db.dao.PassDao
import com.migo.task.model.vo.Pass

@Database(entities = [Pass::class], version = 2)
abstract class PassDatabase : RoomDatabase() {
    abstract fun PassDao(): PassDao
}