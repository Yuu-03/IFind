package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class UserDetailView extends AppCompatActivity {

    FirebaseAuth auth;

    UserRecordAdapterClass adapter;
    List<UserRecordHelperClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    SearchView searchView1;

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

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
                startActivity(new Intent(UserDetailView.this, AdminMain.class));
                finish();
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




}