package com.onblock.myapp.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class UserAppAdapter extends RecyclerView.Adapter<UserAppAdapter.UserViewHolder>  {
    List<AppInfo> apps = new ArrayList();
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_app_item, null, false);
        UserViewHolder viewHolder = new UserViewHolder(vi);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final AppInfo currentAppInfo = apps.get(position);
        holder.app_name.setText(currentAppInfo.getName());
        holder.app_icon.setImageDrawable(AppInfoController.bytes2Drawable(currentAppInfo.getIcon()));
    }

    @Override
    public int getItemCount() {
        return apps == null ? 0 : apps.size();
    }
    public void setGrantedApps(List<AppInfo> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView app_name;
        ImageView app_icon;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            app_name = itemView.findViewById(R.id.app_name);
            app_icon = itemView.findViewById(R.id.app_icon);
        }
    }
    /**
    private LayoutInflater inflater;
    private ArrayList<AppInfo> allowedApps = new ArrayList<>();

    public UserAppAdapter(Context context,  int resource , ArrayList<AppInfo> objects){
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
        AppInfo appInfo = allowedApps.get(i);
        TextView appName = vi.findViewById(R.id.app_name);
        ImageView appIcon = vi.findViewById(R.id.app_icon);
       // appIcon.setImageBitmap(appInfo.getIcon());
        // appIcon.setImageBitmap(Bitmap.createScaledBitmap(AppInfoController.byteToBitmap(appInfo.getIcon()), appIcon.getWidth(), appIcon.getHeight(), false));
        appName.setText(appInfo.getName());
        return vi;
    }**/
}
