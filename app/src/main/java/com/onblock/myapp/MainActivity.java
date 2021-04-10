package com.onblock.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.onblock.myapp.adapters.AdminListAppAdapter;
import com.onblock.myapp.adapters.UserAppAdapter;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.models.AppInfoModel;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    GridView appGrideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appGrideView = findViewById(R.id.gridApps);
        getAllowdedAppList();
    }


    private void getAllowdedAppList() {
        ArrayList<AppInfoModel> allowedAppsList;
        allowedAppsList= AppInfoController.getGrantedAppList(this);
        UserAppAdapter adapter = new UserAppAdapter(this, R.layout.grid_app_item, allowedAppsList);
        appGrideView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void runAdminActivities(View view) {
        Intent intent = new Intent(this, AdminHome.class);
        startActivity(intent);
    }
}