package com.onblock.myapp.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.AppInfoController;
import com.onblock.myapp.data.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class UserAppAdapter extends RecyclerView.Adapter<UserAppAdapter.UserViewHolder> {
    List<AppInfo> apps = new ArrayList();
    private UserAppAdapter.OnItemClickListener listener;

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

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView app_name;
        ImageView app_icon;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            app_name = itemView.findViewById(R.id.app_name);
            app_icon = itemView.findViewById(R.id.app_icon);
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

    public void setOnItemClickListener(UserAppAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
