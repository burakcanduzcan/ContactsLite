package com.burakcanduzcan.contactslite

import android.app.Application
import com.burakcanduzcan.contactslite.data.ContactRoomDatabase

class ContactApplication : Application() {
    val database: ContactRoomDatabase by lazy {
        ContactRoomDatabase.getDatabase(this)
    }
}