package com.onblock.myapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.Group;
import com.onblock.myapp.data.model.User;
import com.onblock.myapp.data.model.relations.AppGroupCrossRef;
import com.onblock.myapp.data.model.relations.AppWithGroups;
import com.onblock.myapp.data.model.relations.GroupWithApps;
import com.onblock.myapp.data.model.relations.GroupWithUsers;

import java.util.List;

@Dao
public interface GrpUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroup(Group group);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppGroupCrossRef(AppGroupCrossRef appGroupCrossRef);

    @Update
    void updateUser(User user);


    @Update
    void updateGroup(Group group);

    @Update
    void updateAppGroupCrossRef(AppGroupCrossRef appGroupCrossRef);

    @Delete
    void deleteUser(User user);


    @Delete
    void deleteGroup(Group group);

    @Delete
    void deleteAppGroupCrossRef(AppGroupCrossRef appGroupCrossRef);


    @Query("SELECT * FROM group_table")
    List<Group> getGroupList();

    @Query("SELECT * FROM AppGroupCrossRef WHERE groupName = :groupName AND packageName = :packageName")
    List<AppGroupCrossRef> getGroupAppsRefList(String groupName, String packageName);

    @Transaction
    @Query("SELECT * FROM group_table WHERE groupName = :groupName")
    List<GroupWithUsers> getGroupWithUsers(String groupName);

    @Transaction
    @Query("SELECT * FROM appInfo_table WHERE packageName = :packageName")
    List<AppWithGroups> getGroupsOfApp(String packageName);

    @Transaction
    @Query("SELECT * FROM group_table WHERE groupName = :groupName")
    List<GroupWithApps> getAppsOfGroup(String groupName);

    @Transaction
    @Query("SELECT groupName FROM user_table WHERE cin = :cin")
    String getGroupForUser(String cin);

    @Transaction
    @Query("SELECT * FROM appInfo_table INNER JOIN AppGroupCrossRef ON  AppGroupCrossRef.packageName = appInfo_table.packageName AND AppGroupCrossRef.groupName = :groupName")
    LiveData<List<AppInfo>> getAppsForGroup(String groupName);

    @Transaction
    @Query("SELECT cin FROM user_table WHERE userName = :userName AND password = :password")
    List<String> getUserId(String userName, String password);

    @Transaction
    @Query("SELECT * FROM appInfo_table INNER JOIN AppGroupCrossRef WHERE appInfo_table.name LIKE  :str AND AppGroupCrossRef.packageName = appInfo_table.packageName AND AppGroupCrossRef.groupName = :groupName  AND isTheContainer = 0 ")
    LiveData<List<AppInfo>> getSearchResultsForGroup(String groupName,String str);
}