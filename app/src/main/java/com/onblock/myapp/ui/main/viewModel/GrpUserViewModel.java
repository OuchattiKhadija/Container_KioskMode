package com.onblock.myapp.ui.main.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.Group;
import com.onblock.myapp.data.model.User;
import com.onblock.myapp.data.model.relations.AppGroupCrossRef;
import com.onblock.myapp.data.model.relations.GroupWithUsers;
import com.onblock.myapp.data.repository.GrpUserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GrpUserViewModel extends AndroidViewModel {
    GrpUserRepository grpUserRepository;

    public GrpUserViewModel(@NonNull @NotNull Application application) {
        super(application);
        grpUserRepository = new GrpUserRepository(application);
    }

    //users
    public void insertUser(User user) {
        grpUserRepository.insertUser(user);
    }

    public void updateUser(User user) {
        grpUserRepository.updateUser(user);
    }

    public void deleteUser(User user) {
        grpUserRepository.deleteUser(user);
    }

    // Groups
    public void insertGroup(Group group) {
        grpUserRepository.insertGroup(group);
    }

    public void updateGroup(Group group) {
        grpUserRepository.updateGroup(group);
    }

    public void deleteGroup(Group group) {
        grpUserRepository.deleteGroup(group);
    }

    // UserAppCrosRef
    public void insertGroupCrossRef(AppGroupCrossRef appGroupCrossRef) {
        grpUserRepository.insertAppGroupCrossRef(appGroupCrossRef);
    }

    public void updateGroupCrossRef(AppGroupCrossRef appGroupCrossRef) {
        grpUserRepository.updateAppGroupCrossRef(appGroupCrossRef);
    }

    public void deleteGroupCrossRef(AppGroupCrossRef appGroupCrossRef) {
        grpUserRepository.deleteAppGroupCrossRef(appGroupCrossRef);
    }

    //Queries
    public List<GroupWithUsers> getGroupWithUsers(String grpName) {
        return grpUserRepository.getGroupWithUsers(grpName);
    }

    public String getGroupForUser(String cin) {
        return grpUserRepository.getGroupForUser(cin);
    }

    public List<Group> getGroupList() {
        return grpUserRepository.getGroupList();
    }

    public List<AppGroupCrossRef> getGroupAppsRefList(String groupName, String packageName) {
        return grpUserRepository.getGroupAppsRefList(groupName, packageName);
    }

    public LiveData<List<AppInfo>> getAppsForGroup(String groupName) {
        return grpUserRepository.getAppsForGroup(groupName);
    }
    public List<String> getUserId(String userName,String password){
        return grpUserRepository.getUserId(userName,password);
    }

    public LiveData<List<AppInfo>> getSearchResultsForGroup(String groupName,String str){
        return grpUserRepository.getSearchResultsForGroup(groupName,str);
    }
}
