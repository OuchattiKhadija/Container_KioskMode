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
import com.onblock.myapp.ui.main.adapter.UserAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView appGrideView;
    AppInfoViewModel appInfoViewModel;
    UserAppAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appGrideView = findViewById(R.id.gridApps);
        adapter = new UserAppAdapter();
        appInfoViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AppInfoViewModel.class);
        Toast.makeText(MainActivity.this, "Apps!", Toast.LENGTH_LONG).show();
        getAllowdedAppList();
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
                    Toast.makeText(MainActivity.this," Granted!", Toast.LENGTH_LONG).show();
                }
            }
        });
        /**
         ArrayList<AppInfo> allowedAppsList;
         allowedAppsList= AppInfoController.getGrantedAppList(this);
         UserAppAdapter adapter = new UserAppAdapter(this, R.layout.grid_app_item, allowedAppsList);
         appGrideView.setAdapter(adapter);
         adapter.notifyDataSetChanged();**/
    }

    /**public void setIsGranted(AppInfo appInfo) {
        appInfo.setNormalUserAllowed(true);
        appInfoViewModel.update(appInfo);

    }**/

    public void runAdminActivities(View view) {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }
}