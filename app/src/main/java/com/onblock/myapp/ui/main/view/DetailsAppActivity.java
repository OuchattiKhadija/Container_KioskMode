package com.onblock.myapp.ui.main.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.AppInfoDao;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class DetailsAppActivity extends AppCompatActivity {

    public static String EXTRA_APP_PACKAGE;
    public static String EXTRA_APP_NAME;
    Button allowBtn, denyBtn;
    AppInfoViewModel appInfoViewModel;
    ImageView appIcon;
    TextView appName, appPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_app);
        getSupportActionBar().hide();
        allowBtn = findViewById(R.id.allow_btn);
        denyBtn = findViewById(R.id.deny_btn);
        appIcon = findViewById(R.id.app_picture);
        appName = findViewById(R.id.app_name);
        appPackage = findViewById(R.id.package_string);
        EXTRA_APP_PACKAGE = getIntent().getStringExtra("EXTRA_APP_PACKAGE");
        EXTRA_APP_NAME = getIntent().getStringExtra("EXTRA_APP_NAME");

        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);
        getAllRequstedPermissions(EXTRA_APP_PACKAGE);
        //out.println("Peeermission " + EXTRA_APP_PACKAGE);
        AppInfo currentAppInfo;
        currentAppInfo = appInfoViewModel.getFromPackage(EXTRA_APP_PACKAGE);
        appIcon.setImageDrawable(AppInfoController.bytes2Drawable(currentAppInfo.getIcon()));
        appName.setText(currentAppInfo.getName());
        appPackage.setText(currentAppInfo.getPackageName());

        allowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminHomeActivity.setifNormalUserAllowed(true, EXTRA_APP_PACKAGE);
            }
        });
        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminHomeActivity.setifNormalUserAllowed(false, EXTRA_APP_PACKAGE);
            }
        });

    }

    public List<String> getAllRequstedPermissions(final String appPackageName) {
        ArrayList<String> allRequstedPermissions = new ArrayList<>();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(appPackageName,
                    PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);
            out.println("Permission ");
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                allRequstedPermissions.add(pi.requestedPermissions[i]);
                out.println("Peeeeermission " + i + " est : " + pi.requestedPermissions[i]);
            }
        } catch (Exception e) {
        }

        return allRequstedPermissions;
    }


}