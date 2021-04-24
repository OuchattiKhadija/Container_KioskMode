package com.onblock.myapp.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.onblock.myapp.R;
import com.onblock.myapp.data.model.AppInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AppInfoController {

    static ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
    static ArrayList<AppInfo> garantedAppsList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    public static ArrayList<AppInfo> getAppInfoList(Activity a) {
        //  List<ApplicationInfo> packs = a.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        List<PackageInfo> packs = a.getPackageManager().getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        final int size = packs.size();
        for (int i = 0; i < size; i++) {
            PackageInfo p = packs.get(i);
            if (p.versionName == null) {
                continue;
            }
            AppInfo appInfo = new AppInfo(
                    p.packageName,
                    p.applicationInfo.loadLabel(a.getPackageManager()).toString(),
                    p.versionName,
                    p.versionCode, AppInfoController.drawable2Bytes((p.applicationInfo.loadIcon(a.getPackageManager()))),
                    false);
            // appInfo.setName(p.applicationInfo.loadLabel(a.getPackageManager()).toString());
            // appInfo.setPackageName(p.packageName);
            // appInfo.setVersionName(p.versionName);
            // appInfo.setVersionCode(p.versionCode) ;
            // appInfo.setIcon(p.applicationInfo.loadIcon(a.getPackageManager()));
            appInfoList.add(appInfo);
        }
        return appInfoList;
    }

    /**
     * public static ArrayList<AppInfo> getGrantedAppList(Activity a) {
     * for (int i = 0;i < appInfoList.size();i++){
     * if (appInfoList.get(i).isNormalUserAllowed()){
     * garantedAppsList.add(appInfoList.get(i));
     * }
     * }
     * return garantedAppsList;
     * }
     **/

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
        Drawable d = (Drawable) bd;
        return d;
    }

    //End======================Convert icon from byte array to drawable=====================================


}

