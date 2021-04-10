package com.onblock.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.onblock.myapp.adapters.AdminListAppAdapter;
import com.onblock.myapp.adapters.UserAppAdapter;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.models.AppInfoModel;

import java.util.ArrayList;

import static java.lang.System.out;

public class AdminHome extends AppCompatActivity {
    ListView appListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        appListView = findViewById(R.id.appList_view);
        getAppList();

    }

    private void getAppList() {
        ArrayList<AppInfoModel> appInfoList;
        appInfoList= AppInfoController.getAppInfoList(this);
        AdminListAppAdapter adapter = new AdminListAppAdapter(this, R.layout.list_app_item, appInfoList);
        appListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}