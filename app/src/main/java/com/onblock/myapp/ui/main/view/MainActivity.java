package com.onblock.myapp.ui.main.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.KioskManager;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.adapter.UserAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView appGrideView;
    AppInfoViewModel appInfoViewModel;
    UserAppAdapter adapter;

    //private ComponentName mAdminComponentName;
    //private DevicePolicyManager mDevicePolicyManager;

    public static KioskManager kioskManager;

    public static Boolean isAdmin;

    @NonNull
    public static final String LOCK_ACTIVITY_KEY = "com.onblock.myapp.ui.main.view.MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kioskManager = new KioskManager(MainActivity.this);
        // mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this);
        //mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        boolean mboolean = false;

        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {
            // do the thing for the first time
            kioskManager.removeActiveAdmine();


        }
        settings = getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("FIRST_RUN", true);
        editor.commit();


        isAdmin = kioskManager.isAdmin();
        if (isAdmin) {
            Toast.makeText(this, R.string.device_owner, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.not_device_owner, Toast.LENGTH_SHORT).show();
        }

        kioskManager.setKioskPolicies(true, isAdmin);

        appGrideView = findViewById(R.id.gridApps);

        adapter = new UserAppAdapter();

        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);

        // Toast.makeText(MainActivity.this, "Apps!", Toast.LENGTH_LONG).show();
        getAllowdedAppList();

        adapter.setOnItemClickListener(new UserAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInfo appInfo) {
                String pn;
                pn = appInfo.getPackageName();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pn);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                } else {
                    Toast.makeText(MainActivity.this, "Package Not found", Toast.LENGTH_LONG);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        kioskManager.setKioskPolicies(true, isAdmin);
        super.onResume();
    }

    private void getAllowdedAppList() {
        appGrideView.setHasFixedSize(true);
        appGrideView.setLayoutManager(new GridLayoutManager(this, 4));
        appGrideView.setAdapter(adapter);
        appInfoViewModel.getAllGrantedApp().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                if (appInfos.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No App is Granted!", Toast.LENGTH_LONG).show();
                    adapter.setGrantedApps(appInfos);
                } else {
                    adapter.setGrantedApps(appInfos);
                }
            }
        });
    }

    /**
     * @RequiresApi(api = Build.VERSION_CODES.M)
     * public void runAdminActivities(View view) {
     * kioskManager.setKioskPolicies(false, isAdmin);
     * Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
     * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     * intent.putExtra(LOCK_ACTIVITY_KEY, false);
     * startActivity(intent);
     * }
     **/
    public void LogInAsAdmin(View view) {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
    }

}