package com.onblock.myapp.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.onblock.myapp.data.AppDb;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.AppInfoDao;

import java.util.List;

public class AppInfoRepository {
    private AppInfoDao appInfoDao;
    private LiveData<List<AppInfo>> allApps;
    private LiveData<List<AppInfo>> allGrantedApps;

    public AppInfoRepository(Application application) {
        AppDb database = AppDb.getDatabaseInstance(application);
        appInfoDao = database.appInfoDao();
        allApps = appInfoDao.getAllApps();
        allGrantedApps=appInfoDao.getAllGrantedApp();
        //  appInfo =  appInfoDao.getFromPackage(pn);

    }

    public void insert(AppInfo appInfo) {
        new InsertAppAsyncTask(appInfoDao).execute(appInfo);
    }

    public void update(AppInfo appInfo) {
        new UpdateAppAsyncTask(appInfoDao).execute(appInfo);
    }

    public void delete(AppInfo appInfo) {
        new DeleteAppAsyncTask(appInfoDao).execute(appInfo);
    }

    public LiveData<List<AppInfo>> getAllApps() {
        return allApps;
    }

    public LiveData<List<AppInfo>> getAllGrantedApp() {
        return allGrantedApps;
    }

    public AppInfo getFromPackage(String pn) {
        return appInfoDao.getFromPackage(pn);
    }

    public void deletePackage(String pn) {
        appInfoDao.deletePackage(pn);
    }


    private static class InsertAppAsyncTask extends AsyncTask<AppInfo, Void, Void> {
        private AppInfoDao appInfoDao;

        private InsertAppAsyncTask(AppInfoDao appInfoDao) {
            this.appInfoDao = appInfoDao;
        }

        @Override
        protected Void doInBackground(AppInfo... appInfos) {
            appInfoDao.insert(appInfos[0]);
            return null;
        }
    }

    private static class UpdateAppAsyncTask extends AsyncTask<AppInfo, Void, Void> {
        private AppInfoDao appInfoDao;

        private UpdateAppAsyncTask(AppInfoDao appInfoDao) {
            this.appInfoDao = appInfoDao;
        }

        @Override
        protected Void doInBackground(AppInfo... appInfos) {
            appInfoDao.update(appInfos[0]);
            return null;
        }
    }

    private static class DeleteAppAsyncTask extends AsyncTask<AppInfo, Void, Void> {
        private AppInfoDao appInfoDao;

        private DeleteAppAsyncTask(AppInfoDao appInfoDao) {
            this.appInfoDao = appInfoDao;
        }

        @Override
        protected Void doInBackground(AppInfo... appInfos) {
            appInfoDao.delete(appInfos[0]);
            return null;
        }
    }

}

