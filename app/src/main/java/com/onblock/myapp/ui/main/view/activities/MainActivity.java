package com.onblock.myapp.ui.main.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.KioskManager;
import com.onblock.myapp.data.model.Group;
import com.onblock.myapp.data.model.User;
import com.onblock.myapp.data.model.relations.GroupWithUsers;
import com.onblock.myapp.ui.main.view.fragments.FragmentGridHomeApp;
import com.onblock.myapp.ui.main.view.fragments.FragmentLogin;
import com.onblock.myapp.ui.main.viewModel.GrpUserViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG ="Test New Tables" ;
    public static KioskManager kioskManager;
    boolean withLogIn;

    static GrpUserViewModel userGrpViewModel;

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;


    @NonNull
    public static final String LOCK_ACTIVITY_KEY = "com.onblock.myapp.ui.main.view.activities.MainActivity";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("PREFS_SCREEN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putBoolean("ifwithLogIn", true);
        editor.commit();

        userGrpViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(GrpUserViewModel.class);
     //   repo = new UserGrpRepository(getApplication());
       initData();
        kioskManager = new KioskManager(MainActivity.this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        List<GroupWithUsers> test;

       /* test = userGrpViewModel.getGroupWithUsers("admins");
        Log.i(TAG, "table => "+test);

        String userTest;
        userTest = userGrpViewModel.getGroupForUser("JK29921");
        Log.i(TAG, "testGroup => "+userTest);*/


        withLogIn  =sharedPreferences.getBoolean("ifwithLogIn", true);
        if (withLogIn) {
            /*
            Fragment fragmentGridHomeApp = new FragmentGridHomeApp();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_main_act, fragmentGridHomeApp);
                            fragmentTransaction.commit();
             */
            FragmentLogin fragmentLogin = new FragmentLogin();
            fragmentTransaction.replace(R.id.fragment_main_act, fragmentLogin);
            fragmentTransaction.commit();
        } else {
            FragmentGridHomeApp fragmentGridHomeApp = new FragmentGridHomeApp();
            fragmentTransaction.replace(R.id.fragment_main_act, fragmentGridHomeApp);
            fragmentTransaction.commit();
        }


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


    }

    @Override
    public void onBackPressed() {
        // nothing to do here
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(MainActivity.this, "Back button long pressed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyLongPress(keyCode, event);
    }

    public void initData() {
        List<Group> groupList = new ArrayList<>();
        groupList.add(new Group("admins", "Admins IT grouuup"));
        groupList.add(new Group("salesagent", "Sales Agent grouuup"));
        groupList.add(new Group("adminstore", "Admin Store grouuup"));
        groupList.add(new Group("test1", "teeeeeeeeeeeeeeest grouuup1"));
        groupList.add(new Group("test2", "teeeeeeeeeeeeeeest grouuup2"));
        groupList.add(new Group("test3", "teeeeeeeeeeeeeeest grouuup3"));

        for (Group group : groupList) {
            userGrpViewModel.insertGroup(group);
        }
        List<User> userList = new ArrayList<>();
        userList.add(new User("JK29921", "Khadija", "Ouchatti", "Admin IT", "ouchattikhad", "admin", "admins"));
        userList.add(new User("JK1111", "Agent", "agent", "sales agent", "agent", "0000", "salesagent"));
        userList.add(new User("JK11110", "Agent2", "agent2", "sales agent", "agent2", "0000", "salesagent"));
        userList.add(new User("JK333", "Store", "Admin", "Store Admin", "adminstore", "0000", "adminstore"));

        for (User user : userList) {
            userGrpViewModel.insertUser(user);
        }
    }


}

