package com.burakcanduzcan.contactslite.data.dao

import androidx.room.*
import com.burakcanduzcan.contactslite.data.entity.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContact(contact: Contact)

    @Update
    fun updateContact(contact: Contact)

    @Delete
    fun delete(contact: Contact)

    @Transaction
    @Query("SELECT * FROM Contact ORDER BY name ASC")
    fun getAllContactsAlphabetically(): Flow<List<Contact>>

    @Transaction
    @Query("SELECT * FROM Contact WHERE id = :contactId")
    fun getContactFromId(contactId: Int): Contact

}