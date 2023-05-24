package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class userlogview extends AppCompatActivity {
    List<LogHelperClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    adminlogadapter adapter;

    Button mBack;

    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogview);

        mBack = findViewById(R.id.back);
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.userLogs);

        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        dataList = new ArrayList<>();
        adapter = new adminlogadapter(getApplicationContext(), dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("UserActivityLogs");
        databaseReference.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    LogHelperClass dataClass = itemSnapshot.getValue(LogHelperClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }

                // Sort the dataList based on timestamp in descending order
                Collections.sort(dataList, new Comparator<LogHelperClass>() {
                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    @Override
                    public int compare(LogHelperClass log1, LogHelperClass log2) {
                        try {
                            return f.parse(log2.timestamp).compareTo(f.parse(log1.getTimestamp()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousActivity(); // Go back to the previous activity
            }
        });


        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adminLogs:
                        startActivity(new Intent(getApplicationContext(), AdminLogView.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.userLogs:
                        return true;

                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        goToPreviousActivity(); // Go back to the previous activity
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

        Intent intent = new Intent(userlogview.this, AdminMain.class);
        startActivity(intent);
        finish(); // Optional: Call finish() to remove the current activity from the stack
    }
}