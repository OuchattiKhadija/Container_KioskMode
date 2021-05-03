package com.onblock.myapp.ui.main.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.PermissionDetails;
import com.onblock.myapp.data.model.PermissionSections;
import com.onblock.myapp.ui.main.adapter.PermissionListAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailsAppActivity extends AppCompatActivity {

    public static String EXTRA_APP_PACKAGE;
    public static String EXTRA_APP_NAME;
    Button allowBtn, denyBtn;
    AppInfoViewModel appInfoViewModel;
    ImageView appIcon;
    List<PermissionSections> sectionList = new ArrayList<>();
    TextView appName, appPackage, openApp, uninstallApp, settingsApp;
    RecyclerView mainRecyclerView;
    PermissionListAdapter adapter;
    List<PermissionDetails> grantedPermList = new ArrayList<>();
    List<PermissionDetails> deniedPermList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_app);
        getSupportActionBar().hide();
        allowBtn = findViewById(R.id.allow_btn);
        denyBtn = findViewById(R.id.deny_btn);
        appIcon = findViewById(R.id.app_picture);
        appName = findViewById(R.id.app_name);
        openApp = findViewById(R.id.open_app);
        uninstallApp = findViewById(R.id.app_uninstall);
        settingsApp = findViewById(R.id.app_settings);
        appPackage = findViewById(R.id.package_string);
        mainRecyclerView = findViewById(R.id.perm_recyclerView);
        EXTRA_APP_PACKAGE = getIntent().getStringExtra("EXTRA_APP_PACKAGE");
        EXTRA_APP_NAME = getIntent().getStringExtra("EXTRA_APP_NAME");

        //get Data From DB and set it on activity
        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);
        AppInfo currentAppInfo;
        currentAppInfo = appInfoViewModel.getFromPackage(EXTRA_APP_PACKAGE);
        appIcon.setImageDrawable(AppInfoController.bytes2Drawable(currentAppInfo.getIcon()));
        appName.setText(currentAppInfo.getName());
        appPackage.setText(currentAppInfo.getPackageName());

        headerFunctions();
        initData();

        //Fil in the recyclerView
        SetOnAdapterPermissionList();
        btnAllowOrDenyApp();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
        //Fil in the recyclerView
        SetOnAdapterPermissionList();
    }

    public void headerFunctions() {
        //set Methodes onClick Listner header button
        openApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(EXTRA_APP_PACKAGE);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                } else {
                    Toast.makeText(DetailsAppActivity.this, "Package Not found", Toast.LENGTH_LONG);
                }
            }
        });

        settingsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getSettingsIntent(EXTRA_APP_PACKAGE);
                startActivity(intent);
            }
        });
        uninstallApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + EXTRA_APP_PACKAGE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void btnAllowOrDenyApp() {
        //set Methodes onClick Listner bottom button
        allowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminHomeActivity.setifNormalUserAllowed(true, EXTRA_APP_PACKAGE);
                redirectToAdminListApps();
            }
        });
        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminHomeActivity.setifNormalUserAllowed(false, EXTRA_APP_PACKAGE);
                redirectToAdminListApps();
            }
        });
    }

    @NonNull
    private Intent getSettingsIntent(String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        return intent;
    }

    public List<PermissionDetails> getAllRequstedPermissions(final String appPackageName) {
        List<PermissionDetails> allRequstedPermissions = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(appPackageName, PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null && packageInfo.requestedPermissions != null) {
            int i = 0;
            for (String permission : packageInfo.requestedPermissions) {
                try {
                    PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, packageManager.GET_META_DATA);
                    PermissionDetails permissionDetails = new PermissionDetails(permissionInfo.name,
                            (packageInfo.requestedPermissionsFlags[i] & packageInfo.REQUESTED_PERMISSION_GRANTED) != 0);
                    allRequstedPermissions.add(permissionDetails);
                    //out.println("La permission " + permissionDetails.getPermissionName() + " Autorisation " + permissionDetails.isGranted());

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        return allRequstedPermissions;
    }

    public List<PermissionDetails> getDeniedPermList() {
        deniedPermList.clear();
        List<PermissionDetails> allPermission = getAllRequstedPermissions(EXTRA_APP_PACKAGE);
        for (PermissionDetails perm : allPermission) {
            if (perm.isGranted() == false) {
                deniedPermList.add(perm);
            }
        }
        return deniedPermList;
    }

    public List<PermissionDetails> getGrantedPermList() {
        grantedPermList.clear();
        List<PermissionDetails> allPermission = getAllRequstedPermissions(EXTRA_APP_PACKAGE);
        for (PermissionDetails perm : allPermission) {
            if (perm.isGranted() == true) {
                grantedPermList.add(perm);
            }
        }
        return grantedPermList;
    }

    public void initData() {
        sectionList.clear();
        sectionList.add(new PermissionSections("Granted", getGrantedPermList()));
        sectionList.add(new PermissionSections("Denied", getDeniedPermList()));
    }


    private void SetOnAdapterPermissionList() {
        adapter = new PermissionListAdapter(sectionList);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setAdapter(adapter);
        //mainRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter.notifyDataSetChanged();

    }

    public void redirectToAdminListApps() {
        Intent intent = new Intent(DetailsAppActivity.this, AdminHomeActivity.class);
        startActivity(intent);
    }

}