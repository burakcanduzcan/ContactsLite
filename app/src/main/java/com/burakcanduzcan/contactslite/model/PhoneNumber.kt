package com.burakcanduzcan.contactslite.model

import android.net.Uri

class PhoneNumber(private val countryCode: String = "90", private val phoneNumber: String) {
    private val uriStart: String = "tel:"
    private val uniqueNumberIdentifier: String = "+"

    fun convertToUri(): Uri {
        return Uri.parse(uriStart + uniqueNumberIdentifier + countryCode + phoneNumber)
    }

    fun printNumber(): String {
        return phoneNumber
    }


}