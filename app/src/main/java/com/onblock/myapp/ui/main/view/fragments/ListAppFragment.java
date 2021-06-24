package com.onblock.myapp.ui.main.view.fragments;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.adapter.AdminListAppAdapter;
import com.onblock.myapp.ui.main.view.activities.AdminHomeActivity;
import com.onblock.myapp.ui.main.view.activities.DetailsAppActivity;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.lang.System.out;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListAppFragment extends Fragment {

    RecyclerView appListView;
    // Create a AppInfoViewModel instance
    static AppInfoViewModel appInfoViewModel;
    AdminListAppAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListAppFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListAppFragment newInstance(String param1, String param2) {
        ListAppFragment fragment = new ListAppFragment();
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

        adapter = new AdminListAppAdapter();

        appInfoViewModel = new ViewModelProvider(this).get(AppInfoViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appListView = view.findViewById(R.id.appList_view);

        adapter.setOnItemClickListener(new AdminListAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInfo appInfo) {
                Intent intent = new Intent(getActivity(), DetailsAppActivity.class);
                intent.putExtra("EXTRA_APP_PACKAGE", appInfo.getPackageName());
                intent.putExtra("EXTRA_APP_NAME", appInfo.getName());
                startActivity(intent);
            }
        });
    }

    private void SetOnAdapterAppListInstalled() {
        appListView.setHasFixedSize(true);
        appListView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        appListView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        listPacksInDevice = AppInfoController.getPackageList(getActivity());
        listPacksInDb = appInfoViewModel.getAllPackages();

        for (String pack : listPacksInDevice) {
            if (!listPacksInDb.contains(pack)) {
                out.println("Cete element n'esxit pas dans la basede donné =>" + pack);
                //add app to db
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    PackageInfo pinfo = getActivity().getPackageManager().getPackageInfo(pack, 0);
                    ApplicationInfo app = getActivity().getPackageManager().getApplicationInfo(pack, 0);
                    AppInfo newAppInfo = new AppInfo(
                            pinfo.packageName,
                            (String) pm.getApplicationLabel(app),
                            pinfo.versionName,
                            pinfo.versionCode,
                            AppInfoController.drawable2Bytes(pm.getApplicationIcon(pack)),
                            false,
                            AppInfoController.getInstalledAppListTest(getActivity()).contains(pinfo.packageName), false);

                    appInfoViewModel.insert(newAppInfo);

                    AppInfo thisApp = appInfoViewModel.getFromPackage(getActivity().getPackageName());
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


}