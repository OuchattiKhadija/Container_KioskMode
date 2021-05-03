package com.onblock.myapp.data.model;

public class PermissionDetails {
    private String permissionName;
    private boolean isGranted;

    public PermissionDetails(String permissionName, boolean isGranted) {
        this.permissionName = permissionName;
        this.isGranted = isGranted;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }


    public boolean isGranted() {
        return isGranted;
    }

    public void setGranted(boolean granted) {
        isGranted = granted;
    }

}
