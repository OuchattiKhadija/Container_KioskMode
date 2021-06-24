package com.onblock.myapp.ui.main.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.onblock.myapp.R;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.relations.AppGroupCrossRef;
import com.onblock.myapp.ui.main.adapter.AdminListAppAdapter;
import com.onblock.myapp.ui.main.adapter.ListAppGrpAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;
import com.onblock.myapp.ui.main.viewModel.GrpUserViewModel;

import java.util.List;

public class AdminListAppCustomPers extends AppCompatActivity {

    RecyclerView custAppListView;
    // Create a AppInfoViewModel instance
    static AppInfoViewModel appInfoViewModel;
    static GrpUserViewModel grpUserViewModel;
    ListAppGrpAdapter adapter;
    public static String EXTRA_GRP_NAME;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_app_custom_pers);
        EXTRA_GRP_NAME = getIntent().getStringExtra("EXTRA_GRP_NAME");
        setTitle("List App For " + EXTRA_GRP_NAME);
        adapter = new ListAppGrpAdapter();
        custAppListView = findViewById(R.id.appList_cust_pers_view);
        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);
        grpUserViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(GrpUserViewModel.class);


        SetOnAdapterAppListInstalled();

    }

    private void SetOnAdapterAppListInstalled() {
        custAppListView.setHasFixedSize(true);
        custAppListView.setLayoutManager(new LinearLayoutManager(this));
        custAppListView.setAdapter(adapter);

        appInfoViewModel.getInstalledApps().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setApps(appInfos);
            }
        });
    }

    public static void setGrpApps(String packageName, Boolean isGroupAllowed){
        if(grpUserViewModel.getGroupAppsRefList(packageName,EXTRA_GRP_NAME).isEmpty()){
            AppGroupCrossRef appGroupCrossRef = new AppGroupCrossRef(packageName,EXTRA_GRP_NAME,isGroupAllowed);
            grpUserViewModel.insertGroupCrossRef(appGroupCrossRef);
        }else{
            AppGroupCrossRef current =  grpUserViewModel.getGroupAppsRefList(packageName,EXTRA_GRP_NAME).get(0);
           //set value of
            current.setAlowed(isGroupAllowed);
            grpUserViewModel.deleteGroupCrossRef(current);
        }
    }

    public static boolean isGroupAllowedChecked(String packageName){
        if(!grpUserViewModel.getGroupAppsRefList(EXTRA_GRP_NAME,packageName).isEmpty()){
            AppGroupCrossRef cur = grpUserViewModel.getGroupAppsRefList(EXTRA_GRP_NAME,packageName).get(0);
            Log.i("testCheck",cur.getGroupName() + " "+ cur.isAlowed());
            return cur.isAlowed();
        }
        return false;
    }
}