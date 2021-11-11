package com.migo.task.model.repository

import com.migo.task.model.api.model.response.Contact
import com.migo.task.model.db.dao.ContactsDao
import javax.inject.Inject

class RoloDbRepository @Inject constructor(private val contactsDao: ContactsDao) {
    fun fetchAllContacts() = contactsDao.getAll()
    fun fetchStarredContacts() = contactsDao.getStarredContacts()
    fun insertContactsData(contacts: ArrayList<Contact>) = contactsDao.insertAll(contacts)
    fun updateContactsData(contact: Contact) = contactsDao.updateFavorite(contact)
}
