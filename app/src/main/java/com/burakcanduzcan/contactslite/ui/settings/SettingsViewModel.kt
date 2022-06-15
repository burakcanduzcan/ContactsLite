package com.burakcanduzcan.contactslite.ui.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.burakcanduzcan.contactslite.R

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val pref: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE)

    fun getDefaultCountryNameCode(): String {
        return pref.getString("defaultCountry", "TR").toString()
    }

    fun setDefaultCountry(countryCode: String) {
        pref.edit().putString("defaultCountry", countryCode).apply()
    }


}