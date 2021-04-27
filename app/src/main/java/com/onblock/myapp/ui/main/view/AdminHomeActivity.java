package com.onblock.myapp.ui.main.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.AppInfoDao;
import com.onblock.myapp.ui.main.adapter.AdminListAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.List;

import static java.lang.System.out;

public class AdminHomeActivity extends AppCompatActivity {

    RecyclerView appListView;
    // Create a AppInfoViewModel instance
    static AppInfoViewModel appInfoViewModel;
    AdminListAppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        appListView = findViewById(R.id.appList_view);
        /**  adapter = new AdminListAppAdapter(this, new AdminListAppAdapter.OnItemCheckListener() {
        @Override public void onItemCheck(AppInfo appInfo) {
        appInfo.setNormalUserAllowed(true);
        appInfoViewModel.update(appInfo);
        //  out.println("lapplication " + appInfo.getName() + "est autorisé " + appInfo.isNormalUserAllowed());
        }

        @Override public void onItemUncheck(AppInfo appInfo) {
        appInfo.setNormalUserAllowed(false);
        appInfoViewModel.update(appInfo);
        }
        });**/
        adapter = new AdminListAppAdapter();
        //appInfoViewModel = ViewModelProviders.of(this).get(AppInfoViewModel.class);

        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);
        //appListView.setLayoutManager(new LinearLayoutManager(this));
        //appListView.setHasFixedSize(true);
        //appListView.setAdapter(adapter);

        boolean mboolean = false;

        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {
            // do the thing for the first time
            List<AppInfo> firstAppList;
            firstAppList = AppInfoController.getAppInfoList(this);
            for (AppInfo app : firstAppList) {
                appInfoViewModel.insert(app);
                out.println("app ajouter a la DB " + app.getName());
            }
            //SetOnAdapterAppList();

            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        } else {
            // other time your app loads
            out.println("execute else statement");
            //SetOnAdapterAppList();
        }
        SetOnAdapterAppList();
        adapter.setOnItemClickListener(new AdminListAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInfo appInfo) {
                Intent intent = new Intent(AdminHomeActivity.this, DetailsAppActivity.class);
                intent.putExtra("EXTRA_APP_PACKAGE", appInfo.getPackageName());
                intent.putExtra("EXTRA_APP_NAME", appInfo.getName());
                startActivity(intent);
            }
        });

    }

    private void SetOnAdapterAppList() {
        appListView.setHasFixedSize(true);
        appListView.setLayoutManager(new LinearLayoutManager(this));
        appListView.setAdapter(adapter);
        appInfoViewModel.getAllApps().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setApps(appInfos);
            }
        });
    }

    /**
     * @Override public void onItemCheck(AppInfo appInfo) {
     * appInfo.setNormalUserAllowed(true);
     * appInfoViewModel.update(appInfo);
     * }
     * @Override public void onItemUncheck(AppInfo appInfo) {
     * appInfo.setNormalUserAllowed(false);
     * appInfoViewModel.update(appInfo);
     * }
     **/
    public static void setifNormalUserAllowed(Boolean itUserAllowed, String packageName) {
        AppInfo appInfo;
        appInfo = appInfoViewModel.getFromPackage(packageName);
        out.println("Before => The package " + packageName + " is " + appInfo.getIsNormalUserAllowed());
        appInfo.setNormalUserAllowed(itUserAllowed);
        appInfoViewModel.update(appInfo);
        out.println("After => The package " + packageName + " is " + appInfo.getIsNormalUserAllowed());
    }

}
