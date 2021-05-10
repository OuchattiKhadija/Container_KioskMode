package com.onblock.myapp.ui.main.view;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.onblock.myapp.controllers.MyDeviceAdminReceiver;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.adapter.UserAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.List;

import static android.os.BatteryManager.BATTERY_PLUGGED_AC;
import static android.os.BatteryManager.BATTERY_PLUGGED_USB;
import static android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS;
import static java.lang.System.out;


public class MainActivity extends AppCompatActivity {
    RecyclerView appGrideView;
    AppInfoViewModel appInfoViewModel;
    UserAppAdapter adapter;

    //private ComponentName mAdminComponentName;
   // private DevicePolicyManager mDevicePolicyManager;

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
        //mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this);
        //mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (kioskManager.getmDevicePolicyManager().isDeviceOwnerApp(getPackageName())) {
            // You are the owner!
            kioskManager.setKioskPolicies(true);
        } else {
            // Please contact your system administrator
            Toast.makeText(this, "Please contact your system administrator", Toast.LENGTH_LONG);

        }

/*
        isAdmin = kioskManager.isAdmin();
        if (isAdmin) {
            Toast.makeText(this, R.string.device_owner, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.not_device_owner, Toast.LENGTH_SHORT).show();
        }

       // kioskManager.setKioskPolicies(true, isAdmin);*/

        appGrideView = findViewById(R.id.gridApps);

        adapter = new UserAppAdapter();

        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);

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

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            Toast.makeText(MainActivity.this, "Long press detected",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    };



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        kioskManager.setKioskPolicies(true);
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        // nothing to do here
        // … really
    }

    @Override
    protected void onPause() {

        //  ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);

        //      activityManager.moveTaskToFront(getTaskId(), 0);
        super.onPause();
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

/**
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setKioskPolicies(boolean enable) {

        this.setRestrictions(enable);
        this.enableStayOnWhilePluggedIn(enable);
        this.setUpdatePolicy(enable);
        this.setAsHomeApp(enable);
        this.setKeyGuardEnabled(enable);

        this.setLockTask(enable);
        this.setImmersiveMode(enable);
    }

    private void setLockTask(boolean start) {
      mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, start ? new String[]{this.getPackageName()} : new String[0]);
/*
        if (start) {
            this.startLockTask();
        } else {
            this.stopLockTask();
        }*/
    /*}

    @RequiresApi(api = Build.VERSION_CODES.M)
    private final void setRestrictions(boolean disallow) {
        this.setUserRestriction("no_safe_boot", disallow);
        this.setUserRestriction("no_factory_reset", disallow);
        this.setUserRestriction("no_add_user", disallow);
        this.setUserRestriction("no_physical_media", disallow);
        this.setUserRestriction("no_adjust_volume", disallow);
        mDevicePolicyManager.setStatusBarDisabled(mAdminComponentName, disallow);
    }

    private final void setUserRestriction(String restriction, boolean disallow) {
        if (disallow) {
            mDevicePolicyManager.addUserRestriction(mAdminComponentName, restriction);
        } else {
            mDevicePolicyManager.clearUserRestriction(mAdminComponentName, restriction);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private final void setUpdatePolicy(boolean enable) {
        if (enable) {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName,
                    SystemUpdatePolicy.createWindowedInstallPolicy(60, 120));
        } else {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName, null);
        }
    }

    private final void setAsHomeApp(boolean enable) {
        if (enable) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
            intentFilter.addCategory("android.intent.category.HOME");
            intentFilter.addCategory("android.intent.category.DEFAULT");
            mDevicePolicyManager.addPersistentPreferredActivity(
                    mAdminComponentName, intentFilter, new ComponentName(this.getPackageName(), MainActivity.class.getName()));
        } else {
            mDevicePolicyManager.clearPackagePersistentPreferredActivities(
                    mAdminComponentName, this.getPackageName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private final void setKeyGuardEnabled(boolean enable) {
        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, !enable);
    }
    //option Stay awake – Screen will never sleep while charging

    private final void enableStayOnWhilePluggedIn(boolean active) {
        if (active) {
            mDevicePolicyManager.setGlobalSetting(
                    mAdminComponentName,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    String.valueOf((BATTERY_PLUGGED_AC | BATTERY_PLUGGED_USB | BATTERY_PLUGGED_WIRELESS)));
        }
    }

    private final void setImmersiveMode(boolean enable) {
        short flags;
        Window window = this.getWindow();
        View view;
        if (enable) {
            flags = (//View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            view = window.getDecorView();
            view.setSystemUiVisibility(flags);
        } else {
            flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        view = window.getDecorView();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        view.setSystemUiVisibility(flags);

    }*/


}

