package com.burakcanduzcan.contactslite.ui.dialPad

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.*
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.data.dao.ContactDao
import com.burakcanduzcan.contactslite.data.entity.Contact
import com.burakcanduzcan.contactslite.model.PhoneNumber

class DialPadViewModel(application: Application, private val contactDao: ContactDao) :
    AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val pref: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE)

    private var _enteredPhoneNumber: MutableLiveData<String> = MutableLiveData("")
    val enteredPhoneNumber: LiveData<String> = _enteredPhoneNumber

    private var selectedCountryCode: String = ""
    private var uriToBeCalled: Uri? = null

    fun checkWhetherDefaultCountryIsSet(): Boolean {
        return pref.getString("defaultCountry", "DEFAULT") != "DEFAULT"
    }

    fun getDefaultCountry(): String {
        return pref.getString("defaultCountry", "TR").toString()
    }

    fun setDefaultCountry(country: String) {
        pref.edit()
            .putString("defaultCountry", country)
            .apply()
    }

    fun addDigit(enteredDigit: Char) {
        _enteredPhoneNumber.value += enteredDigit
    }

    fun addPhoneNumberAsWhole(phoneNumber: String) {
        _enteredPhoneNumber.value = phoneNumber
    }

    fun removeLastDigit() {
        if (_enteredPhoneNumber.value!!.isNotEmpty()) {
            _enteredPhoneNumber.value = _enteredPhoneNumber.value!!.dropLast(1)
        }
    }

    fun removeAllDigits() {
        if (_enteredPhoneNumber.value!!.isNotEmpty()) {
            _enteredPhoneNumber.value = ""
        }
    }

    fun setSelectedCountryCode(countryCode: String) {
        selectedCountryCode = countryCode
    }

    fun clearEnteredPhoneNumber() {
        removeAllDigits()
    }

    fun setUriToBeCalled() {
        if (_enteredPhoneNumber.value!!.isNotEmpty()) {
            uriToBeCalled =
                PhoneNumber(selectedCountryCode, _enteredPhoneNumber.value!!).convertToUri()
        }
    }

    fun getUriToBeCalled(): Uri? {
        return if (uriToBeCalled != null) {
            uriToBeCalled
        } else {
            null
        }
    }

    fun isValidatorEnabled(): Boolean {
        return pref.getBoolean("phoneNumberValidator", false)
    }

    fun getQuickCallIdFromNumber(number: Int): Int {
        return pref.getInt("quickCall$number", -1)
    }

    fun getContactFromId(id: Int): Contact {
        return contactDao.getContactFromId(id)
    }
}

class DialPadViewModelFactory(private val app: Application, private val contactDao: ContactDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DialPadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DialPadViewModel(app, contactDao) as T
        }
        throw IllegalArgumentException("Unknown ViewHolder class")
    }
}
