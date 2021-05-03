package com.onblock.myapp.ui.main.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.AppInfoDao;
import com.onblock.myapp.ui.main.adapter.AdminListAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.List;

import static java.lang.System.out;

public class AdminHomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView appListView;
    // Create a AppInfoViewModel instance
    static AppInfoViewModel appInfoViewModel;
    AdminListAppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        appListView = findViewById(R.id.appList_view);

        adapter = new AdminListAppAdapter();
        //appInfoViewModel = ViewModelProviders.of(this).get(AppInfoViewModel.class);

        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);

        boolean mboolean = false;

        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {
            // do the thing for the first time
            List<AppInfo> firstAppList;
            firstAppList = AppInfoController.getAppInfoList(this);
            for (AppInfo app : firstAppList) {
                appInfoViewModel.insert(app);
                out.println("app ajouter a la DB " + app.getName());
            }
            //SetOnAdapterAppList();

            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        } else {
            // other time your app loads
            out.println("execute else statement");
            //SetOnAdapterAppList();
        }
        SetOnAdapterAppList();
        adapter.setOnItemClickListener(new AdminListAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInfo appInfo) {
                Intent intent = new Intent(AdminHomeActivity.this, DetailsAppActivity.class);
                intent.putExtra("EXTRA_APP_PACKAGE", appInfo.getPackageName());
                intent.putExtra("EXTRA_APP_NAME", appInfo.getName());
                startActivity(intent);
            }
        });

    }

    private void SetOnAdapterAppList() {
        appListView.setHasFixedSize(true);
        appListView.setLayoutManager(new LinearLayoutManager(this));
        appListView.setAdapter(adapter);
        appInfoViewModel.getAllApps().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setApps(appInfos);
            }
        });
    }

    public static void setifNormalUserAllowed(Boolean itUserAllowed, String packageName) {
        AppInfo appInfo;
        appInfo = appInfoViewModel.getFromPackage(packageName);
       // out.println("Before => The package " + packageName + " is " + appInfo.getIsNormalUserAllowed());
        appInfo.setNormalUserAllowed(itUserAllowed);
        appInfoViewModel.update(appInfo);
       // out.println("After => The package " + packageName + " is " + appInfo.getIsNormalUserAllowed());
    }

    @Override
    protected void onDestroy() {
        updateDb();
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        updateDb();
        super.onStart();
    }

    public void updateDb() {
        List<String> listPacksInDevice, listPacksInDb;
        listPacksInDevice = AppInfoController.getPackageList(this);
        //Toast.makeText(AdminHomeActivity.this, "list On Device ", Toast.LENGTH_SHORT).show();
        listPacksInDb = appInfoViewModel.getAllPackages();
        //Toast.makeText(AdminHomeActivity.this, "list On Db ", Toast.LENGTH_SHORT).show();


        for (String pack : listPacksInDevice) {
            if (!listPacksInDb.contains(pack)) {
                out.println("Cete ellement nesxit pas dans la basede donné =>" + pack);
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
                            false);

                    appInfoViewModel.insert(newAppInfo);

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
        /**
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.main_menu, menu);
         MenuItem searchItem = menu.findItem(R.id.menu_search);
         SearchView searchView = (SearchView) searchItem.getActionView();
         searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override public boolean onQueryTextSubmit(String query) {
        Log.d("newText1",query);
        return false;
        }
        @Override public boolean onQueryTextChange(String newText) {
        Log.d("newText",newText);
        adapter.getFilter().filter(newText);
        return false;
        }
        });
         return true;
         */
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
      //  searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null) {
            searchDatabase(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query != null) {
            searchDatabase(query);
        }
        return true;
    }

    private void searchDatabase(String query) {
        String searchQuery = "%"+query+"%";

        appInfoViewModel.getSearchResults(searchQuery).observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                adapter.setApps(appInfos);
            }
        });
    }
    /*
    private SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getDealsFromDb(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    getAppsFromDb(newText);
                    return true;
                }

                private void getAppsFromDb(String searchText) {
                    searchText = "%"+searchText+"%";
                    appInfoViewModel.getSearchResults(DealsSearchActivity.this, searchText)
                            .observe(DealsSearchActivity.this, new Observer<List<AppInfo>>() {
                                @Override
                                public void onChanged(@Nullable List<AppInfo> deals) {
                                    if (deals == null) {
                                        return;
                                    }
                                    AdminListAppAdapter adapter = new DealsListViewAdapter(
                                            DealsSearchActivity.this,
                                            R.layout.deal_item_layout, deals);
                                    listView.setAdapter(adapter);

                                }
                            });
                }
            };*/

}
