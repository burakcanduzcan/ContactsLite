package com.burakcanduzcan.contactslite.ui.contacts

import android.net.Uri
import androidx.lifecycle.*
import com.burakcanduzcan.contactslite.data.dao.ContactDao
import com.burakcanduzcan.contactslite.data.entity.Contact
import com.burakcanduzcan.contactslite.model.PhoneNumber
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ContactsViewModel(private val contactDao: ContactDao) : ViewModel() {
    val allContacts: LiveData<List<Contact>> = contactDao.getAllContactsAlphabetically()
    var uriToBeCalled: Uri? = null

    private fun insertContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.insertContact(contact)
        }
    }

    private fun getNewContactEntry(
        contactName: String,
        contactSurname: String,
        phoneNumber: String,
    ): Contact {
        return Contact(
            name = contactName,
            surname = contactSurname,
            number = phoneNumber
        )
    }

    fun addNewContact(
        contactName: String,
        contactSurname: String,
        phoneNumber: String,
    ) {
        val newContact = getNewContactEntry(contactName, contactSurname, phoneNumber)
        insertContact(newContact)
    }

    fun setUriToBeCalled(contact: Contact) {
        this.uriToBeCalled = PhoneNumber(phoneNumber = contact.number).convertToUri()
    }
}

//boilerplate code, feel free to reuse
class ContactsViewModelFactory(private val contactDao: ContactDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(contactDao) as T
        }
        throw IllegalArgumentException("Unknown ViewHolder class")
    }
}