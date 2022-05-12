package com.burakcanduzcan.contactslite.data.entity

import androidx.room.*
import com.burakcanduzcan.contactslite.model.ContactGroup

@Entity(tableName = "Contact", indices = [Index(value = ["number"], unique = true)])
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "surname")
    val surname: String,
    @ColumnInfo(name = "number")
    val number: String,
) {
    @Ignore
    var groups = ArrayList<ContactGroup>()
}