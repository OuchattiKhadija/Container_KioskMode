package com.onblock.myapp.data.model;

import java.util.ArrayList;

public class PagerObject {
    private ArrayList<AppInfo> appList;

    public PagerObject(ArrayList<AppInfo> appList){
        this.appList = appList;
    }

    public ArrayList<AppInfo> getAppList() {
        return appList;
    }
}
