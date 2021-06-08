package com.onblock.container.ui.main.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.container.R;
import com.onblock.container.controllers.AppInfoController;
import com.onblock.container.controllers.KioskManager;
import com.onblock.container.data.model.AppInfo;
import com.onblock.container.ui.main.adapter.UserAppAdapter;
import com.onblock.container.ui.main.viewModel.AppInfoViewModel;

import java.util.List;

import static java.lang.System.out;


public class MainActivity extends AppCompatActivity {
    RecyclerView appGrideView;
    AppInfoViewModel appInfoViewModel;
    UserAppAdapter adapter;
    EditText searchUser;

    public static KioskManager kioskManager;

    SharedPreferences sharedPreferences;
    View v;


    @NonNull
    public static final String LOCK_ACTIVITY_KEY = "com.onblock.myapp.ui.main.view.MainActivity";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = findViewById(R.id.main_act);


        searchUser = findViewById(R.id.editSearchUser);
        searchUser.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchAppUser(editable);

            }
        });


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

            kioskManager.setKioskPolicies(true);
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
        super.onStart();
        if (kioskManager.getmDevicePolicyManager().isDeviceOwnerApp(getPackageName())) {
            // You are the owner!
            kioskManager.setKioskPolicies(true);
        } else {
            // Please contact your system administrator
            Toast.makeText(this, "Please contact your system administrator , App is not the Owner", Toast.LENGTH_LONG).show();
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("screenPref", Context.MODE_PRIVATE);
        String sCols = sharedPreferences.getString("numbCols", "");
        String sRows = sharedPreferences.getString("numbRows", "");
        int cols, rows;
        if (!sCols.isEmpty() && !sRows.isEmpty()) {
            cols = Integer.parseInt(sCols);
            rows = Integer.parseInt(sRows);
        } else {
            cols = 4;
            rows = 7;
        }

        String pathImg = sharedPreferences.getString("imagePath", "");
        if (!pathImg.isEmpty()) {

            v.setBackground(AppInfoController.uriToDrawableCon(this, Uri.parse(pathImg)));
        }

        Toast.makeText(this, "" + cols + " " + rows, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        // nothing to do here
    }

    private void getAllowdedAppList() {

        appGrideView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        //  gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL); // set Horizontal Orientation
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

    private void searchAppUser(Editable editable) {
        String searchQuery = "%" + editable + "%";

        appInfoViewModel.getSearchResultsForUser(searchQuery).observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setGrantedApps(appInfos);
            }
        });
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(MainActivity.this, "Package Not found", Toast.LENGTH_SHORT);
            out.println("Back button long pressed");
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyLongPress(keyCode, event);
    }


}

