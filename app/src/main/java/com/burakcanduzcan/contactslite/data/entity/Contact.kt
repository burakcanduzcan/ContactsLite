package com.burakcanduzcan.contactslite.data.entity

import androidx.room.*

@Entity(tableName = "Contact", indices = [Index(value = ["number"], unique = true)])
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "countryCode")
    val countryCode: String,
    @ColumnInfo(name = "number")
    val number: String,
    @ColumnInfo(name = "addDate")
    val addDate: String,
    @ColumnInfo(name = "lastUpdated")
    val lastUpdated: String = "never",
    @ColumnInfo(name = "assignedQuickCallNumber")
    val assignedQuickCallNumber: String = "none",
)