package com.burakcanduzcan.contactslite.ui.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.burakcanduzcan.contactslite.data.dao.GroupDao
import com.burakcanduzcan.contactslite.data.entity.Group
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupsViewModel(private val groupDao: GroupDao) : ViewModel() {
    var allGroups: LiveData<List<Group>> = groupDao.getAllGroupsAlphabetically()

    private fun insertGroup(group: Group) {
        viewModelScope.launch {
            groupDao.insertGroup(group)
        }
    }

    private fun getNewGroupEntity(
        groupName: String,
    ): Group {
        return Group(
            name = groupName,
            memberCount = 0
        )
    }

    fun addNewGroup(groupName: String) {
        val newGroup = getNewGroupEntity(groupName)
        insertGroup(newGroup)
    }

    private fun getUpdatedGroupEntity(oldGroup: Group, newGroupName: String): Group {
        return Group(oldGroup.id, newGroupName, oldGroup.memberCount)
    }

    fun editGroup(oldGroup: Group, newGroupName: String) {
        viewModelScope.launch {
            groupDao.updateGroup(getUpdatedGroupEntity(oldGroup, newGroupName))
        }
    }

    fun deleteGroup(group: Group) {
        viewModelScope.launch(Dispatchers.IO) {
            groupDao.deleteGroup(group)
        }
    }
}

//boilerplate code
class GroupsViewModelFactory(private val groupDao: GroupDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupsViewModel(groupDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}