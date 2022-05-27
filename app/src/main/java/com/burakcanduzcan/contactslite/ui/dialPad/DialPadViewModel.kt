package com.burakcanduzcan.contactslite.ui.dialPad

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.burakcanduzcan.contactslite.model.PhoneNumber

class DialPadViewModel : ViewModel() {
    private var _enteredPhoneNumber: MutableLiveData<String> = MutableLiveData("")
    val enteredPhoneNumber: LiveData<String> = _enteredPhoneNumber
    var uriToBeCalled: Uri? = null

    fun addDigit(enteredDigit: Char) {
        _enteredPhoneNumber.value += enteredDigit
    }

    fun removeLastDigit() {
        if (_enteredPhoneNumber.value!!.isNotEmpty()) {
            _enteredPhoneNumber.value = _enteredPhoneNumber.value!!.dropLast(1)
        }
    }

    fun setUriToBeCalled() {
        if (_enteredPhoneNumber.value!!.isNotEmpty()) {
            this.uriToBeCalled =
                PhoneNumber(phoneNumber = _enteredPhoneNumber.value.toString()).convertToUri()
        }
    }
}