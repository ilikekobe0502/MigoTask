package com.migo.task.ui.contacts

import com.migo.task.model.api.model.response.Contact

class ContactsFuncListener(
        /**
         * Favorite button click event
         */
        val onFavoriteClick: (Contact) -> Unit = { },
        /**
         * Item click event
         */
        val onItemClick: (Contact) -> Unit = { },
)