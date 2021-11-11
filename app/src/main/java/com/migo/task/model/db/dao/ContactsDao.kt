package com.migo.task.model.db.dao

import androidx.room.*
import com.migo.task.model.api.model.response.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {
    @Query("SELECT * FROM 'contacts'")
    fun getAll(): Flow<List<Contact>>

    @Query("SELECT * FROM 'contacts' WHERE favorite = 1")
    fun getStarredContacts(): Flow<List<Contact>>

    @Update
    fun updateFavorite(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: ArrayList<Contact>)
}