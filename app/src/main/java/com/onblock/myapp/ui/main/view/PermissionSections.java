package com.onblock.myapp.ui.main.view;

import com.onblock.myapp.data.model.PermissionDetails;

import java.util.List;

public class PermissionSections {
    private String sectionName;
    private List<PermissionDetails> permissionItems;

    public PermissionSections(String sectionName, List<PermissionDetails> permissionItems) {
        this.sectionName = sectionName;
        this.permissionItems = permissionItems;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<PermissionDetails> getPermissionItems() {
        return permissionItems;
    }

    public void setPermissionItems(List<PermissionDetails> permissionItems) {
        this.permissionItems = permissionItems;
    }
}
