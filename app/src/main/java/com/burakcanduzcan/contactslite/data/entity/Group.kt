package com.burakcanduzcan.contactslite.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Group")
data class Group(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val name: String,
)