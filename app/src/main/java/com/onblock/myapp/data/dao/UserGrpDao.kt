package com.onblock.myapp.data.dao

import androidx.room.*
import com.onblock.myapp.data.model.Group
import com.onblock.myapp.data.model.User
import com.onblock.myapp.data.model.relations.AppGroupCrossRef
import com.onblock.myapp.data.model.relations.AppWithGroups
import com.onblock.myapp.data.model.relations.GroupWithApps
import com.onblock.myapp.data.model.relations.GroupWithUsers

@Dao
interface UserGrpDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertGroup(group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAppGroupCrossRef(appGroupCrossRef: AppGroupCrossRef)

    @Update
     fun updateUser(user: User)

    @Update
     fun updateGroup(group: Group)

    @Update
     fun updateAppGroupCrossRef(appGroupCrossRef: AppGroupCrossRef)

    @Delete
     fun deleteUser(user: User)

    @Delete
     fun deleteGroup(group: Group)

    @Delete
     fun deleteAppGroupCrossRef(appGroupCrossRef: AppGroupCrossRef)


    @Transaction
    @Query("SELECT * FROM group_table WHERE groupName = :groupName")
     fun getGroupWithUsers(groupName: String): List<GroupWithUsers>

    @Transaction
    @Query("SELECT * FROM appInfo_table WHERE packageName = :packageName")
     fun getGroupsOfApp(packageName: String): List<AppWithGroups>

    @Transaction
    @Query("SELECT * FROM group_table WHERE groupName = :groupName")
     fun getAppsOfGroup(groupName: String): List<GroupWithApps>
}