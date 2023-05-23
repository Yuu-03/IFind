package com.example.ifind;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.SplashScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    //move to main screen after splashscreen
    private static int SPLASH_SCREEN = 6000;

    //Variables for splashscreen animation
    Animation topAnim, botAnim;

    ImageView logo;
    TextView tagline;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.icon_action_bar);

        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.nav_home:
                getSupportFragmentManager().popBackStack(); // End the current fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_lost:
                getSupportFragmentManager().popBackStack(); // End the current fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LostFragment()).commit();
                break;
            case R.id.nav_found:
                getSupportFragmentManager().popBackStack(); // End the current fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FoundFragment()).commit();
                break;
            case R.id.nav_appre:
                getSupportFragmentManager().popBackStack(); // End the current fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppreFragment()).commit();
                break;
            case R.id.nav_contact:
                getSupportFragmentManager().popBackStack(); // End the current fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().popBackStack(); // End the current fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            goToPreviousActivity();
        }

    }


    private void goToPreviousActivity() {
        Class<?> previousActivity;
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        if (runningTasks != null && runningTasks.size() > 0) {
            ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(0);
            ComponentName componentName = taskInfo.topActivity;
            String topActivityName = componentName.getClassName();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (topActivityName.equals(SplashScreen.class.getName())) {
                    previousActivity = AdminMain.class; // Replace with the appropriate activity you want to navigate to instead of the splash screen
                } else {
                    previousActivity = AdminMain.class; // The previous activity you want to go back to
                }
            }
        } else {
            previousActivity = AdminMain.class; // Default to AdminMain if unable to determine the top activity
        }

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Call finish() to remove the current activity from the stack
    }


}

