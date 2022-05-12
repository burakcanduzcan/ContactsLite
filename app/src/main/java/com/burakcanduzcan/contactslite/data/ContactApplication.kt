package com.burakcanduzcan.contactslite.data

import android.app.Application

class ContactApplication : Application() {
    val database: ContactRoomDatabase by lazy {
        ContactRoomDatabase.getDatabase(this)
    }
}