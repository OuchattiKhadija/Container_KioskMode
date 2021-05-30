package com.onblock.myapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


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

    @ColumnInfo(name = "itCanBeOpned")
    private boolean itCanBeOpned;

    //Constructor

    //before DB
    //  public AppInfo() {
    // }

    public AppInfo(@NonNull String packageName, String name, String versionName, int versionCode, byte[] icon, boolean isNormalUserAllowed, boolean itCanBeOpned) {
        this.packageName = packageName;
        this.name = name;
        this.isNormalUserAllowed = isNormalUserAllowed;
        this.versionName = versionName;
        this.icon = icon;
        this.versionCode = versionCode;
        this.itCanBeOpned = itCanBeOpned;
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

    public boolean itCanBeOpned() {
        return itCanBeOpned;
    }
}

