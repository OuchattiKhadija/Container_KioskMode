package com.onblock.myapp.ui.main.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.onblock.myapp.R;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.adapter.AdminListAppAdapter;
import com.onblock.myapp.ui.main.adapter.UserAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView appGrideView;
    AppInfoViewModel appInfoViewModel;
    UserAppAdapter adapter;
    View main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**main = findViewById(R.id.mainActivity);
         main.setOnLongClickListener(new View.OnLongClickListener() {
        @Override public boolean onLongClick(View view) {
        Toast.makeText(MainActivity.this, "Long-tapped on: ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        return true;
        }
        });**/
        appGrideView = findViewById(R.id.gridApps);
        adapter = new UserAppAdapter();
        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);
        // Toast.makeText(MainActivity.this, "Apps!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MainActivity.this, "Package Not found", Toast.LENGTH_LONG);
                }

            }
        });
    }


    private void getAllowdedAppList() {

        appGrideView.setHasFixedSize(true);
        appGrideView.setLayoutManager(new GridLayoutManager(this, 4));
        appGrideView.setAdapter(adapter);
        appInfoViewModel.getAllGrantedApp().observe(this, new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                if (appInfos.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No App is Granted!", Toast.LENGTH_LONG).show();
                } else {
                    adapter.setGrantedApps(appInfos);
                    //Toast.makeText(MainActivity.this," Granted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void runAdminActivities(View view) {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }
}