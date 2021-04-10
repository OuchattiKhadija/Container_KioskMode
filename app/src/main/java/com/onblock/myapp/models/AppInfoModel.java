package com.onblock.myapp.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class AppInfoModel implements  Comparable<AppInfoModel>{
    private String name;
    private String versionName;
    private Drawable icon;
    private String packageName;
    private int versionCode;
    private boolean isNormalUserAllowed;
    private ArrayList<String> grantedPermissionList = new ArrayList<>();
    private final ArrayList<String> deniedPermissionList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isNormalUserAllowed() {
        return isNormalUserAllowed;
    }

    public void setNormalUserAllowed(boolean normalUserAllowed) {
        isNormalUserAllowed = normalUserAllowed;
    }


    public ArrayList<String> getGrantedPermissionList() {
        return grantedPermissionList;
    }

    public void setGrantedPermissionList(ArrayList<String> grantedPermissionList) {
        this.grantedPermissionList = grantedPermissionList;
    }
    public ArrayList<String> getDeniedPermissionList() {
        return deniedPermissionList;
    }

    @Override
    public int compareTo(AppInfoModel appInfoModel) {
        return name.compareTo(appInfoModel.name);

    }


}
