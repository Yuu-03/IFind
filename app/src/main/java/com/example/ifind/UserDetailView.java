package com.example.ifind;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import android.window.SplashScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDetailView extends AppCompatActivity {

    FirebaseAuth auth;

    UserRecordAdapterClass adapter;
    List<UserRecordHelperClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    SearchView searchView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_view);

        searchView1 = findViewById(R.id.search);
        searchView1.clearFocus();
        Button mBack = findViewById(R.id.back);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new UserRecordAdapterClass(this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    UserRecordHelperClass dataClass = itemSnapshot.getValue(UserRecordHelperClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                    Collections.reverse(dataList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDetailView.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

        if (searchView1 != null) {
            searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchList(newText);
                    return true;
                }
            });

            searchView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView1.onActionViewExpanded();
                }
            });
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousActivity(); // Go back to the previous activity
            }
        });


    }

    @Override
    public void onBackPressed() {
        goToPreviousActivity(); // Go back to the previous activity
    }



    public void searchList (String text){
        ArrayList<UserRecordHelperClass> searchList = new ArrayList<>();
        for (UserRecordHelperClass itemHelperClass : dataList) {
            if (itemHelperClass.getFname().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            } else if (itemHelperClass.getEmail().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            } else if (itemHelperClass.getPhone().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            }
        }
        adapter.searchDataList(searchList);
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

        Intent intent = new Intent(UserDetailView.this, AdminMain.class);
        startActivity(intent);
        finish(); // Optional: Call finish() to remove the current activity from the stack
    }




}