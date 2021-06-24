package com.onblock.myapp.ui.main.view.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.onblock.myapp.R;

import static com.onblock.myapp.ui.main.view.activities.MainActivity.LOCK_ACTIVITY_KEY;

public class LogInActivity extends AppCompatActivity {

    Button logInAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);
/*        logInAdmin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(LogInActivity.this, AdminHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(LOCK_ACTIVITY_KEY, false);
                   // kioskManager.getmDevicePolicyManager().setStatusBarDisabled(kioskManager.getmAdminComponentName(), false);
                    startActivity(intent);
                }
        });*/
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


}