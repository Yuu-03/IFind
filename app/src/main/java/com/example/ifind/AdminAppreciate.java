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

public class AdminAppreciate extends AppCompatActivity {
    private FirebaseAuth auth;
    BottomNavigationView nav;

    AppreciationAdminViewAdapterClass adapter;
    List<AppreciationItemHelperClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    Spinner filterSpinner;
    SearchView searchView1;

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_appreciate);

        auth = FirebaseAuth.getInstance();
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.appre_);
        Button mpostButton = findViewById(R.id.adminPostAppreciate);
        filterSpinner = findViewById(R.id.filter_spinner);
        searchView1 = findViewById(R.id.search);
        searchView1.clearFocus();

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pending_:
                        startActivity(new Intent(getApplicationContext(), pendingRequests.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.approved_:
                        startActivity(new Intent(getApplicationContext(), ApprovedAdmin.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.found_:
                        startActivity(new Intent(getApplicationContext(), FoundAdmin.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.appre_:
                        return true;
                    case R.id.adminProfile:
                        startActivity(new Intent(getApplicationContext(), AdminMain.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        mpostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminAppreciate.this, "Upload Appreciation!" , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminAppreciate.this, AdminPostAppreciate.class));

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new AppreciationAdminViewAdapterClass(this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("AppreciationPost");

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    AppreciationItemHelperClass dataClass = itemSnapshot.getValue(AppreciationItemHelperClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                    Collections.reverse(dataList);
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminAppreciate.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"All","A-Z", "Z-A", "This Year", "This Month", "This Week"});
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                String selectedItem1 = (String) parent.getItemAtPosition(position1);
                searchList1(selectedItem1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected
            }
        });

    }
    public void searchList (String text){
        ArrayList<AppreciationItemHelperClass> searchList = new ArrayList<>();
        for (AppreciationItemHelperClass itemHelperClass : dataList) {
            if (itemHelperClass.getPersonName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            } else if (itemHelperClass.getDatePosted().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            } else if (itemHelperClass.getDepartment().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            }  else if (itemHelperClass.getItemname().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            }
        }
        adapter.searchDataList(searchList);
    }

    public void searchList1(String filterType) {
        databaseReference = FirebaseDatabase.getInstance().getReference("AppreciationPost");
        Query query = databaseReference.orderByChild("personName");
        Query queryTime = databaseReference.orderByChild("timestamp");
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentWeek = calendar.get(Calendar.WEEK_OF_MONTH);

        // Assuming you have a "date" field in your Firebase database for each item
        if (filterType.equals("A-Z")) {

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        AppreciationItemHelperClass dataClass = itemSnapshot.getValue(AppreciationItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                    Collections.sort(dataList, new Comparator<AppreciationItemHelperClass>() {
                        @Override
                        public int compare(AppreciationItemHelperClass item1, AppreciationItemHelperClass item2) {
                            return item1.getPersonName().compareToIgnoreCase(item2.getPersonName());
                        }
                    });
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }else if (filterType.equals("Z-A")) {
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        AppreciationItemHelperClass dataClass = itemSnapshot.getValue(AppreciationItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                    Collections.sort(dataList, new Comparator<AppreciationItemHelperClass>() {
                        @Override
                        public int compare(AppreciationItemHelperClass item1, AppreciationItemHelperClass item2) {
                            return item2.getPersonName().compareToIgnoreCase(item1.getPersonName());
                        }
                    });
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if (filterType.equals("This Year")) {


            queryTime.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        AppreciationItemHelperClass dataClass = itemSnapshot.getValue(AppreciationItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());

                        // Extract the month from the timestamp (assuming it's stored as a valid timestamp)
                        String itemTimestamp = dataClass.getDatePosted();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = sdf.parse(itemTimestamp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar itemCalendar = Calendar.getInstance();
                        if (date != null) {
                            itemCalendar.setTime(date);
                            int itemYear = itemCalendar.get(Calendar.YEAR);
                            // Filter data based on the desired month
                            if (itemYear == currentYear) {
                                dataList.add(dataClass);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else if (filterType.equals("This Month")) {
            queryTime.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        AppreciationItemHelperClass dataClass = itemSnapshot.getValue(AppreciationItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());

                        // Extract the month from the timestamp (assuming it's stored as a valid timestamp)
                        String itemTimestamp = dataClass.getDatePosted();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = sdf.parse(itemTimestamp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar itemCalendar = Calendar.getInstance();
                        if (date != null) {
                            itemCalendar.setTime(date);
                            int itemMonth = itemCalendar.get(Calendar.MONTH);
                            int itemYear = itemCalendar.get(Calendar.YEAR);
                            // Filter data based on the desired month
                            if (itemMonth == currentMonth && itemYear == currentYear) {
                                dataList.add(dataClass);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (filterType.equals("This Week")) {
            queryTime.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        AppreciationItemHelperClass dataClass = itemSnapshot.getValue(AppreciationItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());

                        // Extract the month from the timestamp (assuming it's stored as a valid timestamp)
                        String itemTimestamp = dataClass.getDatePosted();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = sdf.parse(itemTimestamp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar itemCalendar = Calendar.getInstance();
                        if (date != null) {
                            itemCalendar.setTime(date);
                            int itemMonth = itemCalendar.get(Calendar.MONTH);
                            int itemYear = itemCalendar.get(Calendar.YEAR);
                            int itemWeek = itemCalendar.get(Calendar.WEEK_OF_MONTH);
                            // Filter data based on the desired month
                            if (itemMonth == currentMonth && itemYear == currentYear && itemWeek == currentWeek) {
                                dataList.add(dataClass);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (filterType.equals("All")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("AppreciationPost");


            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        AppreciationItemHelperClass dataClass = itemSnapshot.getValue(AppreciationItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                        Collections.reverse(dataList);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            // No valid filter type provided
            return;
        }

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