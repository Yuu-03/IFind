package com.example.ifind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMain extends AppCompatActivity {

    BottomNavigationView nav;
    private FirebaseAuth auth;
    private Button mLogbutt, AdminLogs, mForgotten, mUser;

    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mLogbutt = findViewById(R.id.adminLogoutButt);
        AdminLogs = findViewById(R.id.LogView);
        mForgotten = findViewById(R.id.ForgottenBox);
        mUser = findViewById(R.id.UserDetail);

        auth = FirebaseAuth.getInstance();
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.adminProfile);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pending_:
                        startActivity(new Intent(getApplicationContext(), pendingRequests.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.approved_:
                        startActivity(new Intent(getApplicationContext(), ApprovedAdmin.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.found_:
                        startActivity(new Intent(getApplicationContext(), FoundAdmin.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.appre_:
                        startActivity(new Intent(getApplicationContext(), AdminAppreciate.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.adminProfile:
                        return true;

                }
                return false;
            }
        });
        mLogbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(AdminMain.this, LoginActivity.class));
                finish();
            }
        });

        AdminLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMain.this, AdminLogView.class));
                finish();
            }
        });
        mForgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMain.this, ForgottenView.class));
                finish();
            }
        });
        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMain.this, UserDetailView.class));
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }
}