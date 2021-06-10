package com.onblock.myapp.ui.main.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.KioskManager;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.adapter.UserAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

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
    View root;

    int nCols;

    @NonNull
    public static final String LOCK_ACTIVITY_KEY = "com.onblock.myapp.ui.main.view.MainActivity";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = findViewById(R.id.main_act);


        sharedPreferences = getApplicationContext().getSharedPreferences("PREFS_SCREEN", Context.MODE_PRIVATE);

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
         root = someView.getRootView();
        // Set the color
        //root.setBackgroundColor(getResources().getColor(R.color.custTransparent));

        kioskManager = new KioskManager(MainActivity.this);

        //AppInfoController.clearDeviceOwner(this);

        if (kioskManager.getmDevicePolicyManager().isDeviceOwnerApp(getPackageName())) {
            // You are the owner!

            kioskManager.setKioskPolicies(true);
        } else {
            // Please contact your system administrator
            Toast.makeText(this, "Please contact your system administrator,  App is not the Owner", Toast.LENGTH_LONG).show();
        }


        appGrideView = findViewById(R.id.gridApps);

        adapter = new UserAppAdapter();

        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);



        adapter.setOnItemClickListener(new UserAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInfo appInfo) {
                String pn;
                pn = appInfo.getPackageName();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pn);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                } else {
                    Toast.makeText(MainActivity.this, "Package Not found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Please contact your system administrator , App is not the Owner", Toast.LENGTH_SHORT).show();
        }




        //String pathImg = null;
        String pathImg = sharedPreferences.getString("imgBackground", null);
        if (pathImg != null) {

            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(pathImg);
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            v.setBackground(bd);
        }
        else {
           // Toast.makeText(this, "Path Empty", Toast.LENGTH_SHORT).show();
            v.setBackgroundResource(R.drawable.default_image1);
        }
         nCols = sharedPreferences.getInt("numbCols", 4);

      //  Toast.makeText(this, "" + nCols + " " + nRows, Toast.LENGTH_SHORT).show();
        getAllowdedAppList();
    }

    @Override
    public void onBackPressed() {
        // nothing to do here
    }

    private void getAllowdedAppList() {

        appGrideView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager;

        Toast.makeText(this,"nbCols "+ nCols,Toast.LENGTH_SHORT).show();
        if(nCols !=0){
             gridLayoutManager = new GridLayoutManager(getApplicationContext(), nCols);
            Toast.makeText(this, "ifstatement cols" + nCols, Toast.LENGTH_SHORT).show();

        }
        else {
             gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
            Toast.makeText(this, "else statement cols" + nCols, Toast.LENGTH_LONG).show();

        }
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), nCols);
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
            Toast.makeText(MainActivity.this, "Package Not found", Toast.LENGTH_SHORT).show();
            out.println("Back button long pressed");
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyLongPress(keyCode, event);
    }


}

