package com.onblock.myapp.controllers;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



import androidx.annotation.NonNull;


public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    private static final String TAG = "MyDeviceAdminReceiver";

    public static final ComponentName getComponentName(@NonNull Context context) {
        return new ComponentName(context.getApplicationContext(), MyDeviceAdminReceiver.class);
    }

    public void onLockTaskModeEntering(@NonNull Context context, @NonNull Intent intent, @NonNull String pkg) {
        super.onLockTaskModeEntering(context, intent, pkg);
        Log.d(TAG, "onLockTaskModeEntering");
    }

    public void onLockTaskModeExiting(@NonNull Context context, @NonNull Intent intent) {
        super.onLockTaskModeExiting(context, intent);
        Log.d(TAG, "onLockTaskModeExiting");
    }


}
