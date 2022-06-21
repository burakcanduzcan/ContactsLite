package com.burakcanduzcan.contactslite.data.entity

import androidx.room.*

@Entity(tableName = "Group", indices = [Index(value = ["name"], unique = true)])
data class Group(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "memberCount")
    val memberCount: Int,
)