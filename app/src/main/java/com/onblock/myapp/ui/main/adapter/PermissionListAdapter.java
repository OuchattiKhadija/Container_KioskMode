package com.onblock.myapp.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onblock.myapp.R;
import com.onblock.myapp.data.model.PermissionDetails;
import com.onblock.myapp.ui.main.view.PermissionSections;

import java.util.List;

public class PermissionListAdapter extends RecyclerView.Adapter<PermissionListAdapter.PermissionListViewHolder> {
    List<PermissionSections> sections;

    public PermissionListAdapter(List<PermissionSections> sections) {
        this.sections = sections;
    }

    @NonNull
    @Override
    public PermissionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.perm_section_row, parent, false);
        PermissionListViewHolder viewHolder = new PermissionListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionListViewHolder holder, int position) {
        PermissionSections section = sections.get(position);
        String sectionName = section.getSectionName();
        List<PermissionDetails> permissionsSection  = section.getPermissionItems();
        holder.sectionNameTV.setText(sectionName);
        ChildPermissionListAdapter childPermissionListAdapter = new ChildPermissionListAdapter(permissionsSection);
        holder.childRecyclerView.setAdapter(childPermissionListAdapter);
        if (permissionsSection.isEmpty()){
            holder.noPermission.setText("No "+ section.getSectionName() + " permission found");
        }
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    static class PermissionListViewHolder extends RecyclerView.ViewHolder {
        TextView sectionNameTV,noPermission;
        RecyclerView childRecyclerView;


        public PermissionListViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionNameTV = itemView.findViewById(R.id.sectionName);
            childRecyclerView = itemView.findViewById(R.id.child_recycler_view);
            noPermission = itemView.findViewById(R.id.noPermissions);
        }


    }

}
