package com.migo.task.model.api.model.response


import com.google.gson.annotations.SerializedName

data class Contacts(
    @SerializedName("contacts")
    val contacts: ArrayList<Contact>
)