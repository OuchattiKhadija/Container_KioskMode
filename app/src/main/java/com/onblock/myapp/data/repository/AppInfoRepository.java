package com.onblock.myapp.data.repository;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.onblock.myapp.data.AppDb;
import com.onblock.myapp.data.dao.AppInfoDao;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.view.activities.AdminHomeActivity;

import java.util.List;

public class AppInfoRepository {
    private AppInfoDao appInfoDao;
    private LiveData<List<AppInfo>> allApps;
    private LiveData<List<AppInfo>> allGrantedApps;
    Application application;


    public AppInfoRepository(Application application) {
        AppDb database = AppDb.getDatabaseInstance(application);
        appInfoDao = database.appInfoDao();
        allGrantedApps = appInfoDao.getAllGrantedApp();
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

    public LiveData<List<AppInfo>> getAllASystempps() {
        return appInfoDao.getAllASystempps();
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

    public List<String> getAllPackages() {
        return appInfoDao.getAllPackages();
    }

    public LiveData<List<AppInfo>> getInstalledApps() {
        return appInfoDao.getInstalledApps();
    }

    public void deniedAllApps() {
        appInfoDao.deniedAllApps();
    }

    public LiveData<List<AppInfo>> getSearchResults(String name) {
        return appInfoDao.getSearchResults(name);
    }

    public LiveData<List<AppInfo>> getSearchResultsSystemAPP(String name) {
        return appInfoDao.getSearchResultsSystemAPP(name);
    }

    public LiveData<List<AppInfo>> getSearchResultsForUser(String name) {
        return appInfoDao.getSearchResultsForUser(name);
    }


    private static class InsertAppAsyncTask extends AsyncTask<AppInfo, Void, Void> {
        private AppInfoDao appInfoDao;
        ProgressDialog progressDialog;

        private InsertAppAsyncTask(AppInfoDao appInfoDao) {
            this.appInfoDao = appInfoDao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance

            //AdminHomeActivity.progressDialogShow().show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
          //  AdminHomeActivity.progressDialogShow().dismiss();
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

