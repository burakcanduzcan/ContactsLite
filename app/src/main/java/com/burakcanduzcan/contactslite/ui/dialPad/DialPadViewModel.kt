package com.burakcanduzcan.contactslite.ui.dialPad

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.*
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.model.PhoneNumber

class DialPadViewModel(application: Application) : AndroidViewModel(application) {
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

    fun isValidatorEnabled() : Boolean {
        return pref.getBoolean("phoneNumberValidator", false)
    }
}