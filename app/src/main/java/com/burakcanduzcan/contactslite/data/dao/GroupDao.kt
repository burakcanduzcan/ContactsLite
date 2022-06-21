package com.burakcanduzcan.contactslite.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.burakcanduzcan.contactslite.data.entity.Group

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)

    @Transaction
    @Query("SELECT * FROM `Group` ORDER BY name ASC")
    fun getAllGroupsAlphabetically(): LiveData<List<Group>>

    @Transaction
    @Query("SELECT * FROM `Group` WHERE id=:groupId")
    fun getGroupFromId(groupId: Int): Group

}