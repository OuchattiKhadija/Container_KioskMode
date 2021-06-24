package com.onblock.myapp.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.onblock.myapp.R;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.Group;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GroupUsersAdapter extends ArrayAdapter<Group> {
    private List<Group> grpList;
    private LayoutInflater inflater;

    public GroupUsersAdapter(@NonNull Context context, int resource, List<Group> objects) {
        super(context, resource, objects);
        this.grpList = objects;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return grpList.size();
    }

    @Override
    public Group getItem(int i) {
        return grpList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (view == null)
            vi = inflater.inflate(R.layout.grp_list_item, null);
        Group grp = grpList.get(i);
        TextView tvgrpName = vi.findViewById(R.id.tv_grp_name);
        TextView tvgrpDesc = vi.findViewById(R.id.tv_grp_description);
        tvgrpName.setText(grp.getGroupName());
        tvgrpDesc.setText(grp.getDescription());
        return vi;
    }



}
