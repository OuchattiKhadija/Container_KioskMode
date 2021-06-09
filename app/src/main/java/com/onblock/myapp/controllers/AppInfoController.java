package com.onblock.myapp.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.onblock.myapp.data.model.AppInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AppInfoController {

    static ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();

    public static List<String> getInstalledAppListTest(Activity activity) {
        List<String> list = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = activity.getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo untreatedApp : untreatedAppList) {
            String appPackageName = untreatedApp.activityInfo.packageName;
//    String packageName, String name, String versionName, int versionCode, byte[] icon, boolean isNormalUserAllowed, boolean itCanBeOpned)
            if (!list.contains(appPackageName))
                list.add(appPackageName);
        }
        return list;
    }

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
                    getInstalledAppListTest(a).contains(p.packageName), false);
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

    public static byte[] imagemTratada(byte[] imagem_img){

        while (imagem_img.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 400, 900, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;

    }
    //End======================Convert icon from byte array to drawable=====================================


    public static void clearDeviceOwner(Application application) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) application.getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.clearDeviceOwnerApp(application.getPackageName());
    }





}

