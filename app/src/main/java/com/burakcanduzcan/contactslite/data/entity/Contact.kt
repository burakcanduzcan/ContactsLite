package com.burakcanduzcan.contactslite.data.entity

import androidx.room.*

@Entity(tableName = "Contact", indices = [Index(value = ["number"], unique = true)])
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "number")
    val number: String,
)