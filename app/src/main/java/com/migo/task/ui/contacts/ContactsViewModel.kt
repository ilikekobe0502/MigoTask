package com.migo.task.ui.contacts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.migo.task.enums.FilterType
import com.migo.task.model.api.ApiResult
import com.migo.task.model.api.model.response.Contact
import com.migo.task.model.repository.RoloDbRepository
import com.migo.task.model.repository.RoloRepository
import com.migo.task.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ContactsViewModel @ViewModelInject constructor(
        private var roloRepository: RoloRepository,
        private var roloDbRepository: RoloDbRepository
) : BaseViewModel() {

    private val _contactsResult = MutableLiveData<ApiResult<List<Contact>?>>() // All Contacts liveData
    val contactsResult: LiveData<ApiResult<List<Contact>?>> = _contactsResult

    private val _starredContactsResult = MutableLiveData<ApiResult<List<Contact>?>>() // Starred Contacts liveData
    val starredContactsResult: LiveData<ApiResult<List<Contact>?>> = _starredContactsResult

    private val _currentFilterResult = MutableLiveData<FilterType>() // Current display type
    val currentFilterResult: LiveData<FilterType> = _currentFilterResult


    /**
     * Get all Contacts
     */
    fun getAllContacts() {
        viewModelScope.launch {
            roloRepository.getContacts()
                    .onStart { _contactsResult.value = ApiResult.loading() }
                    .catch { e -> _contactsResult.value = ApiResult.error(e) }
                    .onCompletion { _contactsResult.value = ApiResult.loaded() }
                    .collect { _contactsResult.value = ApiResult.success(it) }
        }
    }

    /**
     * Get starred Contacts
     */
    fun getStarredContacts() {
        viewModelScope.launch {
            roloRepository.getStarredContacts()
                    .onStart { _starredContactsResult.value = ApiResult.loading() }
                    .catch { e -> _starredContactsResult.value = ApiResult.error(e) }
                    .onCompletion { _starredContactsResult.value = ApiResult.loaded() }
                    .collect {
                        _starredContactsResult.value = ApiResult.success(it)
                    }
        }
    }

    /**
     * Update Favorite state
     * @param contact: Contact data,
     */
    fun updateFavorite(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            roloDbRepository.updateContactsData(contact)
        }
    }

    /**
     * Update current Filter state
     * @param type: The type you want to switch
     */
    fun updateFilter(type: FilterType) {
        if (_currentFilterResult.value != type)
            _currentFilterResult.value = type
    }
}