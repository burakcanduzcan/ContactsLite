package com.burakcanduzcan.contactslite

import android.app.Application
import com.burakcanduzcan.contactslite.data.ContactRoomDatabase
import com.burakcanduzcan.contactslite.utils.CustomDebugTree
import timber.log.Timber

class ContactApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(CustomDebugTree())
        }
    }

    val database: ContactRoomDatabase by lazy {
        ContactRoomDatabase.getDatabase(this)
    }
}