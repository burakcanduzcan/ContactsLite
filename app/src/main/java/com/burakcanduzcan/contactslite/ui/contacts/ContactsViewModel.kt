package com.burakcanduzcan.contactslite.ui.contacts

import android.net.Uri
import androidx.lifecycle.*
import com.burakcanduzcan.contactslite.data.dao.ContactDao
import com.burakcanduzcan.contactslite.data.entity.Contact
import com.burakcanduzcan.contactslite.model.PhoneNumber
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDate

class ContactsViewModel(private val contactDao: ContactDao) : ViewModel() {
    val allContacts: LiveData<List<Contact>> = contactDao.getAllContactsAlphabetically()
    var uriToBeCalled: Uri? = null

    fun addNewContact(
        contactName: String,
        countryCode: String,
        phoneNumber: String,
    ) {
        val newContact = getNewContactEntry(contactName, countryCode, phoneNumber)
        insertContact(newContact)
    }

    private fun getNewContactEntry(
        contactName: String,
        contactCountryCode: String,
        contactPhoneNumber: String,
    ): Contact {
        return Contact(
            name = contactName,
            countryCode = contactCountryCode,
            number = contactPhoneNumber,
            addDate = getDate()
        )
    }

    private fun insertContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.insert(contact)
        }
    }

    fun getContactFromId(id: Int): Contact {
        return contactDao.getContactFromId(id)
    }

    fun updateContact(
        oldContact: Contact,
        newName: String,
        newCountryCode: String,
        newNumber: String,
    ) {
        viewModelScope.launch {
            contactDao.update(
                getUpdatedContactEntity(oldContact, newName, newCountryCode, newNumber)
            )
        }
    }

    private fun getUpdatedContactEntity(
        oldContact: Contact,
        newName: String,
        newCountryCode: String,
        newNumber: String,
    ): Contact {
        return Contact(
            oldContact.id,
            name = newName,
            countryCode = newCountryCode,
            number = newNumber,
            oldContact.addDate,
            lastUpdated = getDate(),
            oldContact.assignedQuickCallNumber)
    }

    fun updateContactQuickCallField(oldContact: Contact, quickCallField: String) {
        viewModelScope.launch {
            contactDao.update(
                getQuickCallUpdatedContactEntity(oldContact, quickCallField)
            )
        }
    }

    private fun getQuickCallUpdatedContactEntity(
        oldContact: Contact,
        quickCallField: String,
    ): Contact {
        return Contact(
            oldContact.id,
            oldContact.name,
            oldContact.countryCode,
            oldContact.number,
            oldContact.addDate,
            oldContact.lastUpdated,
            assignedQuickCallNumber = quickCallField
        )
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.delete(contact)
        }
    }

    fun setUriToBeCalled(contact: Contact) {
        this.uriToBeCalled = PhoneNumber("90", contact.number).convertToUri()
    }

    private fun getDate(): String {
        return LocalDate.now().toString()
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