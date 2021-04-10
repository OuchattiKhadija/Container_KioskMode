package com.onblock.myapp.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.onblock.myapp.R;
import com.onblock.myapp.models.AppInfoModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class AppInfoController  {
   static ArrayList<AppInfoModel> appInfoList = new ArrayList<AppInfoModel>();
    static ArrayList<AppInfoModel> garantedAppsList = new ArrayList<AppInfoModel>();
    @SuppressLint("WrongConstant")
    public static ArrayList<AppInfoModel> getAppInfoList(Activity a) {
        //  List<ApplicationInfo> packs = a.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        List<PackageInfo> packs = a.getPackageManager().getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        final int size = packs.size();
        for (int i = 0; i < size; i++) {
            PackageInfo p = packs.get(i);
            if (p.versionName == null) {
                continue;
            }
            AppInfoModel appInfo = new AppInfoModel();
            appInfo.setName(p.applicationInfo.loadLabel(a.getPackageManager()).toString());
            appInfo.setPackageName(p.packageName);
            appInfo.setVersionName(p.versionName);
            appInfo.setVersionCode(p.versionCode) ;
            appInfo.setIcon(p.applicationInfo.loadIcon(a.getPackageManager()));
            appInfoList.add(appInfo);
        }
        return appInfoList;
    }
    public static ArrayList<AppInfoModel> getGrantedAppList(Activity a) {
        for (int i = 0;i < appInfoList.size();i++){
            if (appInfoList.get(i).isNormalUserAllowed()){
                garantedAppsList.add(appInfoList.get(i));
            }
        }
        return garantedAppsList;
    }


}

