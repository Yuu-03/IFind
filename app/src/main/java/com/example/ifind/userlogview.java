package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

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
                List<LogHelperClass> amList = new ArrayList<>();
                List<LogHelperClass> pmList = new ArrayList<>();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    LogHelperClass dataClass = itemSnapshot.getValue(LogHelperClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    String timestamp = dataClass.getTime();

                    // Splitting the timestamp into time and AM/PM
                    String[] parts = timestamp.split(" ");
                    String time = parts[0];
                    String amPm = parts[1];

                    // Check if it is AM or PM and add to respective lists
                    if (amPm.equals("AM")) {
                        amList.add(dataClass);
                    } else if (amPm.equals("PM")) {
                        pmList.add(dataClass);
                    }
                }

                // Sort the AM list in ascending order
                Collections.sort(amList, new Comparator<LogHelperClass>() {
                    DateFormat f = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
                    @Override
                    public int compare(LogHelperClass log1, LogHelperClass log2) {
                        try {
                            return f.parse(log1.getTime().split(" ")[0]).compareTo(f.parse(log2.getTime().split(" ")[0]));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });

                // Sort the PM list in ascending order
                Collections.sort(pmList, new Comparator<LogHelperClass>() {
                    DateFormat f = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
                    @Override
                    public int compare(LogHelperClass log1, LogHelperClass log2) {
                        try {
                            return f.parse(log1.getTime().split(" ")[0]).compareTo(f.parse(log2.getTime().split(" ")[0]));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });

                // Add the sorted PM list after the AM list
                dataList.addAll(amList);
                dataList.addAll(pmList);


                Collections.reverse(dataList);

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
                startActivity(new Intent(userlogview.this, AdminMain.class));
                finish();
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
}