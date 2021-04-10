package com.onblock.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onblock.myapp.AdminHome;
import com.onblock.myapp.R;
import com.onblock.myapp.models.AppInfoModel;

import java.util.ArrayList;

import static java.lang.System.out;

public class AdminListAppAdapter extends ArrayAdapter<AppInfoModel> {

    private ArrayList<AppInfoModel> apps;
    private LayoutInflater inflater;



    public AdminListAppAdapter(Context context,  int resource , ArrayList<AppInfoModel> objects){
        super(context, resource, objects);
        this.apps = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public AppInfoModel getItem(int i) {
        return apps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vi = view;
        if (view == null)
            vi = inflater.inflate(R.layout.list_app_item, null);
        final AppInfoModel appInfo = apps.get(position);
        TextView appName = vi.findViewById(R.id.nameApp);
        TextView appVersion = vi.findViewById(R.id.versionApp);
        ImageView appIcon = vi.findViewById(R.id.iconApp);
        CheckBox cheked = vi.findViewById(R.id.is_Permit);
        cheked.setTag(position);
        cheked.setOnCheckedChangeListener(checkListener);
      /*  cheked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    appInfo.setNormalUserAllowed(true);
                    out.println(appInfo);
                }
            }
        });*/
        appIcon.setImageDrawable(appInfo.getIcon());
        appVersion.setText(appInfo.getVersionName());
        appName.setText(appInfo.getName());
        return vi;
    }


    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            AppInfoModel currentApp;
            int position = (int) buttonView.getTag();
            currentApp = apps.get(position);
            //Affichage de longue durée
            currentApp.setNormalUserAllowed(true);

            out.println("appSelectionner : "+currentApp.getName());

        }
    };
}


