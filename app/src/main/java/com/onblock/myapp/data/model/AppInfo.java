package com.onblock.myapp.data.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "appInfo_table")
public class AppInfo {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "packageName")
    private String packageName;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "isNormalUserAllowed")
    private boolean isNormalUserAllowed;

    @ColumnInfo(name = "versionName")
    private String versionName;

    @ColumnInfo(name = "icon")
    private byte[] icon;

    @ColumnInfo(name = "versionCode")
    private int versionCode;

    //to remove
    @Ignore
    private ArrayList<String> grantedPermissionList = new ArrayList<>();
    @Ignore
    private final ArrayList<String> deniedPermissionList = new ArrayList<>();

    //Constructor

    //before DB
    //  public AppInfo() {
    // }

    public AppInfo(@NonNull String packageName, String name, String versionName, int versionCode, byte[] icon, boolean isNormalUserAllowed) {
        this.packageName = packageName;
        this.name = name;
        this.isNormalUserAllowed = isNormalUserAllowed;
        this.versionName = versionName;
        this.icon = icon;
        this.versionCode = versionCode;
    }


    //Getter&Setter
    public String getName() {
        return name;
    }

    public String getVersionName() {
        return versionName;
    }

    public byte[] getIcon() {
        return icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public boolean getIsNormalUserAllowed() {
        return isNormalUserAllowed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setNormalUserAllowed(boolean isAllowed) {
        isNormalUserAllowed = isAllowed;
    }

    //to remove
    public void setGrantedPermissionList(ArrayList<String> grantedPermissionList) {
        this.grantedPermissionList = grantedPermissionList;
    }


}

