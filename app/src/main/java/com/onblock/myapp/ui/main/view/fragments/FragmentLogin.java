package com.onblock.myapp.ui.main.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.onblock.myapp.R;
import com.onblock.myapp.ui.main.view.activities.AdminHomeActivity;
import com.onblock.myapp.ui.main.viewModel.GrpUserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String userGrp;
    GrpUserViewModel grpUserViewModel;

    public FragmentLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLogin.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLogin newInstance(String param1, String param2) {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText userNameEdit, passwordEdit;
        Button btnLogIn;

        userNameEdit = view.findViewById(R.id.txt_username);
        passwordEdit = view.findViewById(R.id.txt_password);

        btnLogIn = view.findViewById(R.id.logInAdmin);

        grpUserViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(GrpUserViewModel.class);


        btnLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String editUsername, editPassword;
                editUsername = String.valueOf(userNameEdit.getText());
                editPassword = String.valueOf(passwordEdit.getText());
                String groupId;
                if (!getLoginExistgetCIN(editUsername, editPassword).isEmpty()) {

                    groupId = grpUserViewModel.getGroupForUser(grpUserViewModel.getUserId(editUsername, editPassword).get(0));
                    if (groupId.equals("admins")) {
                        Toast.makeText(getContext(), groupId, Toast.LENGTH_SHORT).show();
                        Intent toAdminHome = new Intent(getActivity(), AdminHomeActivity.class);
                        startActivity(toAdminHome);
                    } else {
                        Bundle args = new Bundle();
                        args.putString("GroupId", groupId);
                        Fragment fragmentGridHomeApp = new FragmentGridHomeApp();
                        fragmentGridHomeApp.setArguments(args);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_main_act, fragmentGridHomeApp);
                        fragmentTransaction.commit();

                    }
                } else {
                    Toast.makeText(getActivity(), "User name or Password incorrect", Toast.LENGTH_LONG).show();
                }
/*
                if (username.equals("admin") && password.equals("admin")) {
                    Toast.makeText(getContext(), "Admiiiiiiiiiiiiiin", Toast.LENGTH_SHORT).show();
                    Intent toAdminHome = new Intent(getActivity(), AdminHomeActivity.class);
                    startActivity(toAdminHome);
                } else if (listUsers.contains(username)){
                    switch (username) {
                        case "agent1":
                            userGrp = username;
                            break;
                        case "agent2":
                            userGrp = username;
                            break;
                        case "storeAdmin":
                            userGrp = username;
                            Toast.makeText(getActivity(), "User name is "+ username, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }

                    Fragment fragment = new FragmentGridHomeApp();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_main_act, fragment);
                    fragmentTransaction.commit();

                }else{
                    Toast.makeText(getActivity(), "User name or password incorect", Toast.LENGTH_LONG).show();

                }*/
                userNameEdit.setText("");
                passwordEdit.setText("");
            }
        });
    }

    public List<String> getLoginExistgetCIN(String username, String password) {
        return grpUserViewModel.getUserId(username, password);
    }

    
}