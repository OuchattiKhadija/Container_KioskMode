package com.onblock.myapp.ui.main.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.onblock.myapp.R;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.Group;
import com.onblock.myapp.ui.main.adapter.AdminListAppAdapter;
import com.onblock.myapp.ui.main.adapter.GroupUsersAdapter;
import com.onblock.myapp.ui.main.viewModel.GrpUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {


    private ListView grpListView;
    GrpUserViewModel grpUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        setTitle("Group List");

        grpUserViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(GrpUserViewModel.class);

        grpListView = findViewById(R.id.lv_grp);
        getGrpList();

    }

    private void getGrpList() {

        List<Group> grpList = grpUserViewModel.getGroupList();


        GroupUsersAdapter adapter = new GroupUsersAdapter(this, R.layout.grp_list_item, grpList);
        grpListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        grpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Group grp = grpList.get(position);
                Toast.makeText(GroupActivity.this, grp.getGroupName(), Toast.LENGTH_SHORT).show();
                Intent toCustList = new Intent(GroupActivity.this, AdminListAppCustomPers.class);
                toCustList.putExtra("EXTRA_GRP_NAME", grp.getGroupName());
                startActivity(toCustList);
            }
        });

    }
}