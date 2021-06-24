package com.onblock.myapp.ui.main.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.adapter.UserAppAdapter;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;
import com.onblock.myapp.ui.main.viewModel.GrpUserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGridHomeApp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGridHomeApp extends Fragment {

    AppInfoViewModel appInfoViewModel;
    GrpUserViewModel grpUserViewModel;
    UserAppAdapter adapter;
    RecyclerView appGrideView;
    SharedPreferences sharedPreferences;
    int nCols;
    String groupId;
    private boolean withLogIn;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentGridHomeApp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGridHomeApp.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGridHomeApp newInstance(String param1, String param2) {
        FragmentGridHomeApp fragment = new FragmentGridHomeApp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        adapter = new UserAppAdapter();
        appInfoViewModel = new ViewModelProvider(this).get(AppInfoViewModel.class);
        grpUserViewModel = new ViewModelProvider(this).get(GrpUserViewModel.class);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("PREFS_SCREEN", Context.MODE_PRIVATE);
        nCols = sharedPreferences.getInt("numbCols", 4);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        withLogIn = sharedPreferences.getBoolean("ifwithLogIn", true);
        groupId = getArguments().getString("GroupId");
        return inflater.inflate(R.layout.fragment_grid_home_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText searchUser;
        appGrideView = view.findViewById(R.id.gridApps);
        searchUser = view.findViewById(R.id.editSearchUser);
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

        getAllowdedApptList();

/*
        if (withLogIn) {
            //Retrieve the Group ID

            getAllowdedAppSpecifList(groupId);
        } else {
            getAllowdedAppDefaultList();
        }*/


        adapter.setOnItemClickListener(new UserAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInfo appInfo) {
                String pn;
                pn = appInfo.getPackageName();
                Intent launchApp = getActivity().getPackageManager().getLaunchIntentForPackage(pn);
                if (launchApp != null) {
                    startActivity(launchApp);//null pointer check in case package name was not found
                } else {
                    Toast.makeText(getActivity(), "Package Not found", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getAllowdedApptList() {

        appGrideView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager;

        // Toast.makeText(getActivity(), "nbCols " + nCols, Toast.LENGTH_SHORT).show();
        if (nCols != 0) {
            gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), nCols);
            //Toast.makeText(getActivity(), "ifstatement cols" + nCols, Toast.LENGTH_SHORT).show();

        } else {
            gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 4);
            // Toast.makeText(getActivity(), "else statement cols" + nCols, Toast.LENGTH_LONG).show();

        }
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), nCols);
        appGrideView.setLayoutManager(gridLayoutManager);

        appGrideView.setAdapter(adapter);
        if (withLogIn) {
            grpUserViewModel.getAppsForGroup(groupId).observe(getViewLifecycleOwner(), new Observer<List<AppInfo>>() {
                @Override
                public void onChanged(@Nullable List<AppInfo> appInfos) {
                    if (appInfos.isEmpty()) {
                        Toast.makeText(getActivity(), "No App is Granted!", Toast.LENGTH_SHORT).show();
                        adapter.setGrantedApps(appInfos);
                    } else {
                        adapter.setGrantedApps(appInfos);
                    }
                }
            });
        } else {
            appInfoViewModel.getAllGrantedApp().observe(getViewLifecycleOwner(), new Observer<List<AppInfo>>() {
                @Override
                public void onChanged(@Nullable List<AppInfo> appInfos) {
                    if (appInfos.isEmpty()) {
                        Toast.makeText(getActivity(), "No App is Granted!", Toast.LENGTH_SHORT).show();
                        adapter.setGrantedApps(appInfos);
                    } else {
                        adapter.setGrantedApps(appInfos);
                    }
                }
            });
        }

    }


    private void searchAppUser(Editable editable) {
        String searchQuery = "%" + editable + "%";
        if (!withLogIn) {
            appInfoViewModel.getSearchResultsForUser(searchQuery).observe(this, new Observer<List<AppInfo>>() {
                @Override
                public void onChanged(@Nullable List<AppInfo> appInfos) {
                    adapter.setGrantedApps(appInfos);
                }
            });
        } else {
            grpUserViewModel.getSearchResultsForGroup(groupId, searchQuery).observe(this, new Observer<List<AppInfo>>() {
                @Override
                public void onChanged(@Nullable List<AppInfo> appInfos) {
                    adapter.setGrantedApps(appInfos);
                }
            });
        }

    }

    private void getAllowdedAppSpecifList(String groupId) {

        appGrideView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager;

        Toast.makeText(getActivity(), "nbCols " + nCols, Toast.LENGTH_SHORT).show();
        if (nCols != 0) {
            gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), nCols);
            // Toast.makeText(getActivity(), "ifstatement cols" + nCols, Toast.LENGTH_SHORT).show();

        } else {
            gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 4);
            //Toast.makeText(getActivity(), "else statement cols" + nCols, Toast.LENGTH_LONG).show();

        }
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), nCols);
        appGrideView.setLayoutManager(gridLayoutManager);

        appGrideView.setAdapter(adapter);
        grpUserViewModel.getAppsForGroup(groupId).observe(getViewLifecycleOwner(), new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(@Nullable List<AppInfo> appInfos) {
                if (appInfos.isEmpty()) {
                    Toast.makeText(getActivity(), "No App is Granted!", Toast.LENGTH_SHORT).show();
                    adapter.setGrantedApps(appInfos);
                } else {
                    adapter.setGrantedApps(appInfos);
                }
            }
        });
    }


}