package com.burakcanduzcan.contactslite.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GroupMember")
data class GroupMember(
    @PrimaryKey
    val id: String,
    val contactId: String,
    val groupId: String,
)