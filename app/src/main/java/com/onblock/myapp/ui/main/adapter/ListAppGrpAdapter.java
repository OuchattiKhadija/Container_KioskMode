package com.onblock.myapp.ui.main.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.view.activities.AdminHomeActivity;
import com.onblock.myapp.ui.main.view.activities.AdminListAppCustomPers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListAppGrpAdapter extends RecyclerView.Adapter<ListAppGrpAdapter.AdminCustListViewHolder> {

    List<AppInfo> apps = new ArrayList();
    private AdminListAppAdapter.OnItemClickListener listener;

    @NonNull
    @NotNull
    @Override
    public AdminCustListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_app_item, null, false);
        ListAppGrpAdapter.AdminCustListViewHolder viewHolder = new ListAppGrpAdapter.AdminCustListViewHolder(vi);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminCustListViewHolder holder, int position) {
        final AppInfo currentAppInfo = apps.get(position);
        holder.appName.setText(currentAppInfo.getName());
        holder.appPackage.setText(currentAppInfo.getPackageName());
        holder.appIcon.setImageDrawable(AppInfoController.bytes2Drawable(currentAppInfo.getIcon()));
        if (AdminListAppCustomPers.isGroupAllowedChecked(currentAppInfo.getPackageName())) {
            holder.checked.setChecked(true);
            Log.i("onBindViewHolder", "is chked"+AdminListAppCustomPers.isGroupAllowedChecked(currentAppInfo.getPackageName()));
        }else{
            holder.checked.setChecked(false);
            Log.i("onBindViewHolder", "is chked"+AdminListAppCustomPers.isGroupAllowedChecked(currentAppInfo.getPackageName()));
        }
        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checked.isChecked()) {
                   // AdminHomeActivity.setifNormalUserAllowed(true, currentAppInfo.getPackageName());
                    AdminListAppCustomPers.setGrpApps(currentAppInfo.getPackageName(),true);
                    Toast.makeText(view.getContext(), "Active",Toast.LENGTH_SHORT).show();
                } else {
                    AdminListAppCustomPers.setGrpApps(currentAppInfo.getPackageName(),true);
                    Toast.makeText(view.getContext(), "Desactive",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public AppInfo getAppAt(int position) {
        return apps.get(position);
    }


    @Override
    public int getItemCount() {
        return apps == null ? 0 : apps.size();
    }

    public void setApps(List<AppInfo> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    public class AdminCustListViewHolder extends RecyclerView.ViewHolder {
        TextView appName, appPackage;
        ImageView appIcon;
        SwitchCompat checked;

        public AdminCustListViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.nameApp);
            appPackage = itemView.findViewById(R.id.packageName);
            appIcon = itemView.findViewById(R.id.iconApp);
            checked = itemView.findViewById(R.id.is_Permit);
            //this.setIsRecyclable(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //get adapter position of the cerd clicked
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(apps.get(position));
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(AppInfo appInfo);
    }

    public void setOnItemClickListener(AdminListAppAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
