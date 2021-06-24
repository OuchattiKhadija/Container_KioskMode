package com.onblock.myapp.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.onblock.myapp.data.AppDb;
import com.onblock.myapp.data.dao.GrpUserDao;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.Group;
import com.onblock.myapp.data.model.User;
import com.onblock.myapp.data.model.relations.AppGroupCrossRef;
import com.onblock.myapp.data.model.relations.GroupWithUsers;

import java.util.List;

public class GrpUserRepository {

    GrpUserDao grpUserDao;

    public GrpUserRepository(Application application) {
        AppDb database = AppDb.getDatabaseInstance(application);
        grpUserDao = database.grpUserDao();
    }

    //----------------users---------------

    public void insertUser(User user) {
        new GrpUserRepository.InsertUserAsyncTask(grpUserDao).execute(user);
    }

    public void updateUser(User user) {
        new GrpUserRepository.UpdateUserAsyncTask(grpUserDao).execute(user);
    }

    public void deleteUser(User user) {
        new GrpUserRepository.DeleteUserAsyncTask(grpUserDao).execute(user);
    }


    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private GrpUserDao grpUserDao;

        private InsertUserAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            grpUserDao.insertUser(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private GrpUserDao grpUserDao;

        private UpdateUserAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            grpUserDao.updateUser(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private GrpUserDao grpUserDao;

        private DeleteUserAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            grpUserDao.deleteUser(users[0]);
            return null;
        }
    }


    //----------------Groups---------------


    public void insertGroup(Group group) {
        new GrpUserRepository.InsertGroupAsyncTask(grpUserDao).execute(group);
    }

    public void updateGroup(Group group) {
        new GrpUserRepository.UpdateGroupAsyncTask(grpUserDao).execute(group);
    }

    public void deleteGroup(Group group) {
        new GrpUserRepository.DeleteGroupAsyncTask(grpUserDao).execute(group);
    }


    private static class InsertGroupAsyncTask extends AsyncTask<Group, Void, Void> {
        private GrpUserDao grpUserDao;

        private InsertGroupAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            grpUserDao.insertGroup(groups[0]);
            return null;
        }
    }

    private static class UpdateGroupAsyncTask extends AsyncTask<Group, Void, Void> {
        private GrpUserDao grpUserDao;

        private UpdateGroupAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            grpUserDao.updateGroup(groups[0]);
            return null;
        }
    }

    private static class DeleteGroupAsyncTask extends AsyncTask<Group, Void, Void> {
        private GrpUserDao grpUserDao;

        private DeleteGroupAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            grpUserDao.deleteGroup(groups[0]);
            return null;
        }
    }


    //----------------AppGroupCrossRef---------------


    public void insertAppGroupCrossRef(AppGroupCrossRef appGroupCrossRef) {
        new GrpUserRepository.InsertAppGroupCrossRefAsyncTask(grpUserDao).execute(appGroupCrossRef);
    }

    public void updateAppGroupCrossRef(AppGroupCrossRef appGroupCrossRef) {
        new GrpUserRepository.UpdateAppGroupCrossRefAsyncTask(grpUserDao).execute(appGroupCrossRef);
    }

    public void deleteAppGroupCrossRef(AppGroupCrossRef appGroupCrossRef) {
        new GrpUserRepository.DeleteAppGroupCrossRefAsyncTask(grpUserDao).execute(appGroupCrossRef);
    }


    private static class InsertAppGroupCrossRefAsyncTask extends AsyncTask<AppGroupCrossRef, Void, Void> {
        private GrpUserDao grpUserDao;

        private InsertAppGroupCrossRefAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(AppGroupCrossRef... appGroupCrossRefs) {
            grpUserDao.insertAppGroupCrossRef(appGroupCrossRefs[0]);
            return null;
        }
    }

    private static class UpdateAppGroupCrossRefAsyncTask extends AsyncTask<AppGroupCrossRef, Void, Void> {
        private GrpUserDao grpUserDao;

        private UpdateAppGroupCrossRefAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(AppGroupCrossRef... appGroupCrossRefs) {
            grpUserDao.updateAppGroupCrossRef(appGroupCrossRefs[0]);
            return null;
        }
    }

    private static class DeleteAppGroupCrossRefAsyncTask extends AsyncTask<AppGroupCrossRef, Void, Void> {
        private GrpUserDao grpUserDao;

        private DeleteAppGroupCrossRefAsyncTask(GrpUserDao grpUserDao) {
            this.grpUserDao = grpUserDao;
        }

        @Override
        protected Void doInBackground(AppGroupCrossRef... appGroupCrossRefs) {
            grpUserDao.deleteAppGroupCrossRef(appGroupCrossRefs[0]);
            return null;
        }
    }


    //----------------Queries---------------

    public List<GroupWithUsers> getGroupWithUsers(String grpName) {
        return grpUserDao.getGroupWithUsers(grpName);
    }

    public String getGroupForUser(String cin){
        return grpUserDao.getGroupForUser(cin);
    }

    public List<Group> getGroupList(){
        return grpUserDao.getGroupList();
    }

    public List<AppGroupCrossRef> getGroupAppsRefList(String groupName,String packageName){
        return grpUserDao.getGroupAppsRefList(groupName,packageName);
    }

    public LiveData<List<AppInfo>> getAppsForGroup(String groupName){
        return grpUserDao.getAppsForGroup(groupName);
    }

    public List<String> getUserId(String userName,String password){
        return grpUserDao.getUserId(userName,password);
    }
   public LiveData<List<AppInfo>> getSearchResultsForGroup(String groupName,String str){
        return grpUserDao.getSearchResultsForGroup(groupName,str);
   }

}
