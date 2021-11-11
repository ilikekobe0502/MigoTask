package com.migo.task.model.repository

import com.migo.task.model.api.ApiService
import com.migo.task.model.api.model.response.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException

class RoloRepository constructor(private val apiService: ApiService, private val dbRepository: RoloDbRepository) {

    suspend fun getRemoteContacts(): Flow<List<Contact>> {
        return flowOf(apiService.getUsers())
                .map { result ->
                    if (!result.isSuccessful) throw HttpException(result)
                    val data = result.body()?.contacts
                    dbRepository.insertContactsData(data!!)
                    return@map data
                }.flowOn(Dispatchers.IO)
    }

    fun getContacts(): Flow<List<Contact>> {
        return dbRepository.fetchAllContacts().flatMapConcat {
            if (it.isEmpty()) {
                getRemoteContacts().flatMapConcat { dbRepository.fetchAllContacts() }
            } else {
                flowOf(it)
            }
        }
    }

    fun getStarredContacts(): Flow<List<Contact>> {
        return dbRepository.fetchStarredContacts()
    }
}