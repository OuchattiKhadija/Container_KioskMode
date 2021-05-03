package com.onblock.myapp.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.data.model.PermissionDetails;

import java.util.ArrayList;
import java.util.List;

public class ChildPermissionListAdapter extends RecyclerView.Adapter<ChildPermissionListAdapter.ViewHolder> {
    List<PermissionDetails> perms;

    public ChildPermissionListAdapter(List<PermissionDetails> perms) {
        this.perms = perms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemCount()==0){

        }else{
            holder.permissionName.setText(perms.get(position).getPermissionName());
        }

    }

    @Override
    public int getItemCount() {
        return perms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView permissionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            permissionName = itemView.findViewById(R.id.permissionName);

        }
    }
}
