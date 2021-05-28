package com.onblock.myapp.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.view.MainActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class AppInfoController {

    static ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();

    @SuppressLint("WrongConstant")
    public static ArrayList<AppInfo> getAppInfoList(Activity a) {
        //  List<ApplicationInfo> packs = a.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        List<PackageInfo> packs = a.getPackageManager().getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        final int size = packs.size();
        for (int i = 0; i < size; i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo app = null;
            try {
                app = a.getPackageManager().getApplicationInfo(p.packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (p.versionName == null) {
                continue;
            }
            AppInfo appInfo = new AppInfo(
                    p.packageName,
                    p.applicationInfo.loadLabel(a.getPackageManager()).toString(),
                    p.versionName,
                    p.versionCode, AppInfoController.drawable2Bytes((p.applicationInfo.loadIcon(a.getPackageManager()))),
                    false,
                    (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0 | (app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
            appInfoList.add(appInfo);
        }
        return appInfoList;
    }

    public static List<String> getPackageList(Activity a) {
        List<String> packList = new ArrayList<>();
        //  List<ApplicationInfo> packs = a.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        @SuppressLint("WrongConstant")
        List<PackageInfo> packs = a.getPackageManager().getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        for (PackageInfo pack : packs) {
            packList.add(pack.packageName);
        }
        return packList;
    }

    //Start======================Convert icon from drawable to byte array====================================
    public static byte[] drawable2Bytes(Drawable d) {
        Bitmap bitmap = drawable2Bitmap(d);
        return bitmap2Bytes(bitmap);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    //End======================Convert icon from drawable to byte array=====================================

    //Start======================Convert icon from byte array to drawable===================================
    public static Drawable bytes2Drawable(byte[] b) {
        Bitmap bitmap = bytes2Bitmap(b);
        return bitmap2Drawable(bitmap);
    }

    public static Bitmap bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }

        return null;
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        @SuppressWarnings("deprecation")
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = bd;
        return d;
    }
    //End======================Convert icon from byte array to drawable=====================================

    public static void killAllBackroundApps(Activity activity) {

        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = activity.getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);
        out.println("lee package 1 " + packages.get(0).packageName);
        out.println("lee package 3 " + packages.get(2).packageName);

        ActivityManager mActivityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            } else if (packageInfo.packageName.equals(activity.getPackageName())) {
                continue;
            } else {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
                out.println("lee package " + packageInfo.packageName);
            }
        }
    }

    public static void killAllBackroundApps(Application application) {
        Context context = application.getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            out.println("Taskss " + tasks);
            if (tasks != null && tasks.size() > 0) {
                //tasks.get(0).setExcludeFromRecents(true);
               // for (ActivityManager.AppTask task : tasks){
                for (int i=1; i < tasks.size(); i++ ){
                    tasks.get(i).finishAndRemoveTask();
                }
            }
        }
    }

    public static void clearDeviceOwner(Application application) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) application.getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.clearDeviceOwnerApp(application.getPackageName());
    }

    public static int calculateNoOfRows(Context context, float columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenHightDp = displayMetrics.heightPixels / displayMetrics.density;
        int noOfRows = (int) (screenHightDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        out.println("nuOfRows " + noOfRows);
        return noOfRows;
    }
    public static void runShellCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

}

