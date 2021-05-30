package com.onblock.myapp.ui.main.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.repository.AppInfoRepository;

import java.util.List;

public class AppInfoViewModel extends AndroidViewModel {
    private AppInfoRepository repository;
    private LiveData<List<AppInfo>> allApps;

    public AppInfoViewModel(@NonNull Application application) {
        super(application);
        repository = new AppInfoRepository(application);
    }

    public void insert(AppInfo appInfo) {
        repository.insert(appInfo);
    }

    public void update(AppInfo appInfo) {
        repository.update(appInfo);
    }

    public void delete(AppInfo appInfo) {
        repository.delete(appInfo);
    }

    // Use LiveData for getting all the data from the database
    public LiveData<List<AppInfo>> getAllASystempps() {
        return repository.getAllASystempps();
    }

    public LiveData<List<AppInfo>> getAllGrantedApp() {
        return repository.getAllGrantedApp();
    }

    public AppInfo getFromPackage(String pn) {
        return repository.getFromPackage(pn);
    }

    public void deletePackage(String pn) {
        repository.deletePackage(pn);
    }

    public List<String> getAllPackages() {
        return repository.getAllPackages();
    }

    public LiveData<List<AppInfo>> getSearchResults(String name) {
        return repository.getSearchResults(name);
    }

    public LiveData<List<AppInfo>> getSearchResultsSystemAPP(String name) {
        return repository.getSearchResultsSystemAPP(name);
    }

    public void deniedAllApps() {
        repository.deniedAllApps();
    }

    public LiveData<List<AppInfo>> getInstalledApps() {
        return repository.getInstalledApps();
    }

    public LiveData<List<AppInfo>> getSearchResultsForUser(String name) {
        return repository.getSearchResultsForUser(name);
    }

}