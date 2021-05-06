package com.onblock.myapp.controllers;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.Window;

import androidx.annotation.RequiresApi;

import com.onblock.myapp.ui.main.view.MainActivity;

import static android.content.Context.DEVICE_POLICY_SERVICE;
import static android.os.BatteryManager.BATTERY_PLUGGED_AC;
import static android.os.BatteryManager.BATTERY_PLUGGED_USB;
import static android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS;

public class KioskManager {
    Activity activity;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponentName;


    public KioskManager(Activity activity) {
        this.activity = activity;
        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(activity);
        mDevicePolicyManager = (DevicePolicyManager) activity.getSystemService(DEVICE_POLICY_SERVICE);    // Initializing device policy manager
    }

    public void removeActiveAdmine(){
        mDevicePolicyManager.removeActiveAdmin(mAdminComponentName);
    }

    public final boolean isAdmin() {
        return mDevicePolicyManager.isDeviceOwnerApp(activity.getPackageName());
        //return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public final void setKioskPolicies(boolean enable, boolean isAdmin) {
        if (isAdmin) {
            this.setRestrictions(enable);
            this.enableStayOnWhilePluggedIn(enable);
            this.setUpdatePolicy(enable);
            this.setAsHomeApp(enable);
            this.setKeyGuardEnabled(enable);
        }

        this.setLockTask(enable, isAdmin);
        this.setImmersiveMode(enable);
    }

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

    private final void enableStayOnWhilePluggedIn(boolean active) {
        if (active) {
            mDevicePolicyManager.setGlobalSetting(
                    mAdminComponentName,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    String.valueOf((BATTERY_PLUGGED_AC | BATTERY_PLUGGED_USB | BATTERY_PLUGGED_WIRELESS)));
        }
    }

    public final void setLockTask(boolean start, boolean isAdmin) {
        if (isAdmin) {
            mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, start ? new String[]{activity.getPackageName()} : new String[0]);
        }
        if (start) {
            activity.startLockTask();
        } else {
            activity.stopLockTask();
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

    //https://stackoverflow.com/questions/10904841/avoid-securityexception-because-of-no-active-admin-owned-by
    private final void setAsHomeApp(boolean enable) {
        if (enable) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
            intentFilter.addCategory("android.intent.category.HOME");
            intentFilter.addCategory("android.intent.category.DEFAULT");
            mDevicePolicyManager.addPersistentPreferredActivity(
                    mAdminComponentName, intentFilter, new ComponentName(activity.getPackageName(), MainActivity.class.getName()));
        } else {
            mDevicePolicyManager.clearPackagePersistentPreferredActivities(
                    mAdminComponentName, activity.getPackageName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private final void setKeyGuardEnabled(boolean enable) {
        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, !enable);
    }

    private final void setImmersiveMode(boolean enable) {
        short flags;
        Window window = activity.getWindow();
        View view;
        if (enable) {
            flags = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
            view = window.getDecorView();
            view.setSystemUiVisibility(flags);
        } else {
            flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        view = window.getDecorView();
        view.setSystemUiVisibility(flags);

    }

}