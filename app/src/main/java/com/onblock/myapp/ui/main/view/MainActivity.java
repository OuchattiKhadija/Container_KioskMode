package com.onblock.myapp.ui.main.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
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

import com.ft.possystem.PosSystemManager;
import com.onblock.myapp.R;
import com.onblock.myapp.controllers.KioskManager;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.adapter.UserAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;


public class MainActivity extends AppCompatActivity {
    RecyclerView appGrideView;
    AppInfoViewModel appInfoViewModel;
    UserAppAdapter adapter;

    public static KioskManager kioskManager;


    @NonNull
    public static final String LOCK_ACTIVITY_KEY = "com.onblock.myapp.ui.main.view.MainActivity";

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Now get a handle to any View contained
        // within the main layout you are using
        View someView = findViewById(R.id.gridApps);
        // Find the root view
        View root = someView.getRootView();
        // Set the color
        //root.setBackgroundColor(getResources().getColor(R.color.custTransparent));

        kioskManager = new KioskManager(MainActivity.this);

        //AppInfoController.clearDeviceOwner(this);

        if (kioskManager.getmDevicePolicyManager().isDeviceOwnerApp(getPackageName())) {
            // You are the owner!
          //  kioskManager.setKioskPolicies(true);
        } else {
            // Please contact your system administrator
            Toast.makeText(this, "Please contact your system administrator,  App is not the Owner", Toast.LENGTH_LONG);
        }


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
                    Toast.makeText(MainActivity.this, "Package Not found", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            Toast.makeText(MainActivity.this, "Long press detected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        if (kioskManager.getmDevicePolicyManager().isDeviceOwnerApp(getPackageName())) {
            // You are the owner!
        //    kioskManager.setKioskPolicies(true);
        } else {
            // Please contact your system administrator
            Toast.makeText(this, "Please contact your system administrator , App is not the Owner", Toast.LENGTH_LONG).show();
        }
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        // nothing to do here
    }

    private void getAllowdedAppList() {

        appGrideView.setHasFixedSize(true);
        int nuOfRows = (int) sizeScreen() / 190;
        out.println("Liigne  "+nuOfRows);
       //out.println ("siiize "+appInfoViewModel.getAllGrantedApp().getValue().size());
        //int mNoOfColumns = AppInfoController.calculateNoOfRows(this,120);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), nuOfRows);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL); // set Horizontal Orientation
        appGrideView.setLayoutManager(gridLayoutManager);

        appGrideView.setAdapter(adapter);
        appInfoViewModel.getAllGrantedApp().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                if (appInfos.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No App is Granted!", Toast.LENGTH_SHORT).show();
                    adapter.setGrantedApps(appInfos);
                } else {
                    adapter.setGrantedApps(appInfos);
                }
            }
        });
    }

    public int sizeScreen() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        out.println("heiight ==> " + height);
        return height;

    }
}

