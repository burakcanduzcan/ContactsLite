package com.burakcanduzcan.contactslite.model

var listContacts = ArrayList<Contact>()

class Contact(
    val name: String,
    val surname: String,
    val number: String
) {
    val groups = ArrayList<ContactGroup>()
}