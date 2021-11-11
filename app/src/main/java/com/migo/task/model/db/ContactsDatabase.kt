package com.migo.task.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.migo.task.model.api.model.response.Contact
import com.migo.task.model.db.dao.ContactsDao

@Database(entities = [Contact::class], version = 1)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun ContactsDao(): ContactsDao
}