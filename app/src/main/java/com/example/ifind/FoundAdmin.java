package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class FoundAdmin extends AppCompatActivity {
    private FirebaseAuth auth;
    BottomNavigationView nav;

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundadmin);
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.found_);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pending_:
                        startActivity(new Intent(getApplicationContext(), pendingRequests.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.approved_:
                        startActivity(new Intent(getApplicationContext(), ApprovedAdmin.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.found_:
                        return true;
                    case R.id.adminProfile:
                        startActivity(new Intent(getApplicationContext(), AdminMain.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }


    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            auth.signOut();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }






}