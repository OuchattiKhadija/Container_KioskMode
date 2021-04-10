package com.onblock.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onblock.myapp.R;
import com.onblock.myapp.models.AppInfoModel;

import java.util.ArrayList;

public class UserAppAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<AppInfoModel> allowedApps = new ArrayList<>();

    public UserAppAdapter(Context context,  int resource , ArrayList<AppInfoModel> objects){
        super();
        this.allowedApps = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allowedApps.size();
    }

    @Override
    public Object getItem(int i) {
        return allowedApps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (view == null)
            vi = inflater.inflate(R.layout.grid_app_item, null);
        AppInfoModel appInfo = allowedApps.get(i);
        TextView appName = vi.findViewById(R.id.app_name);
        ImageView appIcon = vi.findViewById(R.id.app_icon);
        appIcon.setImageDrawable(appInfo.getIcon());
        appName.setText(appInfo.getName());
        return vi;
    }
}
