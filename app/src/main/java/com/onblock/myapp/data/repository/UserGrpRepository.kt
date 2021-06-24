package com.onblock.myapp.data.repository

import android.app.Application
import com.onblock.myapp.data.AppDb
import com.onblock.myapp.data.dao.UserGrpDao
import com.onblock.myapp.data.model.Group
import com.onblock.myapp.data.model.User
import com.onblock.myapp.data.model.relations.AppGroupCrossRef
import com.onblock.myapp.data.model.relations.GroupWithUsers

class UserGrpRepository(application: Application) {

    val userGrpDao: UserGrpDao;

    init {
        var database: AppDb = AppDb.getDatabaseInstance(application)
        this.userGrpDao = AppDb.getDatabaseInstance(application).userGrpDao()
    }

    //Insert
     fun insertUser(user: User) {userGrpDao.insertUser(user)}

     fun insertGroup(group: Group) = userGrpDao.insertGroup(group)

     fun insertAppGroupCrossRef(appGroupCrossRef: AppGroupCrossRef) =
        userGrpDao.insertAppGroupCrossRef(appGroupCrossRef)


    // Update
     fun updateUser(user: User) = userGrpDao.updateUser(user)

     fun updateGroup(group: Group) = userGrpDao.updateGroup(group)

     fun updateAppGroupCrossRef(appGroupCrossRef: AppGroupCrossRef) =
        userGrpDao.updateAppGroupCrossRef(appGroupCrossRef)


    //Delete
     fun deleteUser(user: User) = userGrpDao.insertUser(user)

     fun deleteGroup(group: Group) = userGrpDao.insertGroup(group)

     fun deleteAppGroupCrossRef(appGroupCrossRef: AppGroupCrossRef) =
        userGrpDao.insertAppGroupCrossRef(appGroupCrossRef)

    fun getGroupWithUsers(groupName: String) {
       userGrpDao.getGroupWithUsers(groupName);
    }
}