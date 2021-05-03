package com.onblock.myapp.ui.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.ui.main.view.AdminHomeActivity;
import com.onblock.myapp.ui.main.view.MainActivity;
import com.onblock.myapp.ui.main.viewModel.AppInfoViewModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class AdminListAppAdapter extends RecyclerView.Adapter<AdminListAppAdapter.AdminViewHolder> {

    List<AppInfo> apps = new ArrayList();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_app_item, null, false);
        AdminViewHolder viewHolder = new AdminViewHolder(vi);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminViewHolder holder, int position) {
        final AppInfo currentAppInfo = apps.get(position);
        holder.appName.setText(currentAppInfo.getName());
        holder.appPackage.setText(currentAppInfo.getPackageName());
        holder.appIcon.setImageDrawable(AppInfoController.bytes2Drawable(currentAppInfo.getIcon()));
        holder.checked.setChecked(apps.get(position).getIsNormalUserAllowed());
        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checked.isChecked()) {
                    AdminHomeActivity.setifNormalUserAllowed(true, currentAppInfo.getPackageName());
                } else {
                    AdminHomeActivity.setifNormalUserAllowed(false, currentAppInfo.getPackageName());
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


    class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView appName, appPackage;
        ImageView appIcon;
        Switch checked;

        public AdminViewHolder(@NonNull View itemView) {
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}


