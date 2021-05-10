package com.onblock.myapp.ui.main.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.onblock.myapp.R;
import com.onblock.myapp.controllers.KioskManager;

import static com.onblock.myapp.ui.main.view.MainActivity.LOCK_ACTIVITY_KEY;

public class LogInActivity extends AppCompatActivity {

    Button logInAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        logInAdmin = findViewById(R.id.logInAdmin);
        logInAdmin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                    //MainActivity.setKioskPolicies(false);
                    Intent intent = new Intent(LogInActivity.this, AdminHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(LOCK_ACTIVITY_KEY, false);
                    startActivity(intent);
                }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}