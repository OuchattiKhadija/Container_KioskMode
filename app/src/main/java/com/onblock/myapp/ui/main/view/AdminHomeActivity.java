package com.onblock.myapp.ui.main.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.controllers.KioskManager;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.adapter.AdminListAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.List;

import static java.lang.System.out;

public class AdminHomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView appListView;
    // Create a AppInfoViewModel instance
    static AppInfoViewModel appInfoViewModel;
    AdminListAppAdapter adapter;

    public boolean aBoolean = false;
    private long backPressedTime;
    private Toast backToast;

    public static KioskManager kioskManager;

    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        appListView = findViewById(R.id.appList_view);

        progressDialog = new ProgressDialog(AdminHomeActivity.this);
        //progressDialog.setMessage("Loading..."); // Setting Message
        //progressDialog.setTitle("ProgressDialog"); // Setting Title
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner


        adapter = new AdminListAppAdapter();

        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);


        boolean mboolean = false;

        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {

            //progressDialog.setCancelable(false);
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setContentView(R.layout.progress_dialog);
            //set transparent background
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            // do the thing for the first time
            //initProgressDialog().show();
            List<AppInfo> firstAppList;
            firstAppList = AppInfoController.getAppInfoList(this);
            for (AppInfo app : firstAppList) {
                appInfoViewModel.insert(app);
                out.println("app ajouter a la DB " + app.getName());
            }
            progressDialog.dismiss();
            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        } else {
            // other time your app loads
            out.println("execute else statement");
            //SetOnAdapterAppList();
        }

        if (!aBoolean) {
            SetOnAdapterAppListInstalled();
        } else {
            SetOnAdapterSystemAppList();
        }


        adapter.setOnItemClickListener(new AdminListAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInfo appInfo) {
                Intent intent = new Intent(AdminHomeActivity.this, DetailsAppActivity.class);
                intent.putExtra("EXTRA_APP_PACKAGE", appInfo.getPackageName());
                intent.putExtra("EXTRA_APP_NAME", appInfo.getName());
                startActivity(intent);
            }
        });
        kioskManager = new KioskManager(this);
    }

    @Override
    protected void onStop() {
        updateDb();
        //AppInfoController.killAllBackroundApps(this);
        //AppInfoController.killAllBackroundApps(this.getApplication());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        updateDb();
        super.onDestroy();
        // ActivityManager.getRecentTasks();
    }

    @Override
    protected void onStart() {
        updateDb();
        super.onStart();
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            goToHome();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit Admin Page ", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    private void goToHome() {
        Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void SetOnAdapterAppListInstalled() {
        appListView.setHasFixedSize(true);
        appListView.setLayoutManager(new LinearLayoutManager(this));
        appListView.setAdapter(adapter);
        appInfoViewModel.getInstalledApps().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setApps(appInfos);
            }
        });
    }

    private void SetOnAdapterSystemAppList() {
        appListView.setHasFixedSize(true);
        appListView.setLayoutManager(new LinearLayoutManager(this));
        appListView.setAdapter(adapter);
        appInfoViewModel.getAllASystempps().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setApps(appInfos);
            }
        });
    }

    public static void setifNormalUserAllowed(Boolean itUserAllowed, String packageName) {
        AppInfo appInfo;
        appInfo = appInfoViewModel.getFromPackage(packageName);
        appInfo.setNormalUserAllowed(itUserAllowed);
        appInfoViewModel.update(appInfo);
    }

    public void updateDb() {
        List<String> listPacksInDevice, listPacksInDb;
        listPacksInDevice = AppInfoController.getPackageList(this);
        listPacksInDb = appInfoViewModel.getAllPackages();

        for (String pack : listPacksInDevice) {
            if (!listPacksInDb.contains(pack)) {
                out.println("Cete element n'esxit pas dans la basede donné =>" + pack);
                //add app to db
                try {
                    PackageManager pm = getPackageManager();
                    PackageInfo pinfo = getPackageManager().getPackageInfo(pack, 0);
                    ApplicationInfo app = this.getPackageManager().getApplicationInfo(pack, 0);
                    AppInfo newAppInfo = new AppInfo(
                            pinfo.packageName,
                            (String) pm.getApplicationLabel(app),
                            pinfo.versionName,
                            pinfo.versionCode,
                            AppInfoController.drawable2Bytes(pm.getApplicationIcon(pack)),
                            false,
                            AppInfoController.getInstalledAppListTest(this).contains(pinfo.packageName), false);

                    appInfoViewModel.insert(newAppInfo);

                    AppInfo thisApp = appInfoViewModel.getFromPackage(this.getPackageName());
                    thisApp.setIsTheContainer(true);
                    appInfoViewModel.update(thisApp);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        for (String pack : listPacksInDb) {
            if (!listPacksInDevice.contains(pack)) {
                out.println("Cete ellement à été desinstaller =>" + pack);
                //Remove app from db
                appInfoViewModel.deletePackage(pack);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.denyAllApps:
                appInfoViewModel.deniedAllApps();
                return true;

            case R.id.SystemApp:
                if (item.isChecked()) {
                    aBoolean = false;
                    item.setChecked(false);
                    SetOnAdapterAppListInstalled();
                    Toast.makeText(AdminHomeActivity.this, "Hide System Apps ", Toast.LENGTH_SHORT).show();
                } else {
                    item.setChecked(true);
                    aBoolean = true;
                    SetOnAdapterSystemAppList();
                    Toast.makeText(AdminHomeActivity.this, "Show System Apps ", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.enableStatBar:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item.setTitle("Disable StatusBar ");
                    Toast.makeText(this, " StatusBar Enable", Toast.LENGTH_SHORT).show();
                    kioskManager.getmDevicePolicyManager().setStatusBarDisabled(kioskManager.getmAdminComponentName(), false);
                } else {
                    item.setChecked(false);
                    kioskManager.getmDevicePolicyManager().setStatusBarDisabled(kioskManager.getmAdminComponentName(), true);
                    item.setTitle("Enable StatusBar");
                    Toast.makeText(this, "StatusBar Disable", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.exitKiosk:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    AppInfoController.clearDeviceOwner(getApplication());
                    // item.setTitle("Enable Kiosk Mode ");
                   /* Intent intent = new Intent(this, MainActivity.class);
                    intent.removeCategory("android.intent.category.HOME");
                    intent.removeCategory("android.intent.category.DEFAULT");
                    kioskManager.setKioskPolicies(false);*/
                }

            case R.id.settings:
               Intent intent = new Intent(this, SettingsActivity.class);
               startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (aBoolean) {
            if (query != null) {
                searchSystemApp(query);
            }
            return true;
        } else {
            if (query != null) {
                searchDatabase(query);
            }
            return true;
        }
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (aBoolean) {
            if (query != null) {
                searchSystemApp(query);
            }
            return true;
        } else {
            if (query != null) {
                searchDatabase(query);
            }
            return true;
        }
    }

    private void searchDatabase(String query) {
        String searchQuery = "%" + query + "%";

        appInfoViewModel.getSearchResults(searchQuery).observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setApps(appInfos);
            }
        });
    }

    private void searchSystemApp(String query) {

        String searchQuery = "%" + query + "%";

        appInfoViewModel.getSearchResultsSystemAPP(searchQuery).observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setApps(appInfos);
            }
        });
    }

    private ProgressDialog initProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(AdminHomeActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        //  progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        return progressDialog;
    }

}
