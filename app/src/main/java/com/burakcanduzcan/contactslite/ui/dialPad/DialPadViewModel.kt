package com.burakcanduzcan.contactslite.ui.dialPad

import android.net.Uri
import androidx.lifecycle.*
import com.burakcanduzcan.contactslite.model.PhoneNumber

class DialPadViewModel : ViewModel() {
    private var _enteredPhoneNumber: MutableLiveData<String> = MutableLiveData("")
    val enteredPhoneNumber: LiveData<String> = _enteredPhoneNumber

    private var uriToBeCalled: Uri? = null
    private var selectedCountryCode: String = ""

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
}