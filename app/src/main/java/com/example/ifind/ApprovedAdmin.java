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

public class ApprovedAdmin extends AppCompatActivity {
    private FirebaseAuth auth;
    BottomNavigationView nav;

    AdapterClass adapter;
    List<ItemHelperClass> dataList;
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
        setContentView(R.layout.activity_approvedadmin);

        auth = FirebaseAuth.getInstance();
        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.approved_);
        Button mpostButton = findViewById(R.id.adminLostPost);
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
                        return true;
                    case R.id.found_:
                        startActivity(new Intent(getApplicationContext(), FoundAdmin.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.appre_:
                        startActivity(new Intent(getApplicationContext(), AdminAppreciate.class));
                        finish();
                        overridePendingTransition(0,0);
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
                Toast.makeText(ApprovedAdmin.this, "Upload Lost Items!" , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ApprovedAdmin.this, AdminPostLost.class));

            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new AdapterClass(this, dataList, false, true, false, false, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("Approved");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ItemHelperClass dataClass = itemSnapshot.getValue(ItemHelperClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                    Collections.reverse(dataList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ApprovedAdmin.this, "error", Toast.LENGTH_SHORT).show();
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
        ArrayList<ItemHelperClass> searchList = new ArrayList<>();
        for (ItemHelperClass itemHelperClass : dataList) {
            if (itemHelperClass.getItemName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            } else if (itemHelperClass.getLocation().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            } else if (itemHelperClass.getDate().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(itemHelperClass);
            }
        }
        adapter.searchDataList(searchList);
    }

    public void searchList1(String filterType) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Approved");
        Query query = databaseReference.orderByChild("itemName");
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
                        ItemHelperClass dataClass = itemSnapshot.getValue(ItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                    Collections.sort(dataList, new Comparator<ItemHelperClass>() {
                        @Override
                        public int compare(ItemHelperClass item1, ItemHelperClass item2) {
                            return item1.getItemName().compareToIgnoreCase(item2.getItemName());
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
                        ItemHelperClass dataClass = itemSnapshot.getValue(ItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                    Collections.sort(dataList, new Comparator<ItemHelperClass>() {
                        @Override
                        public int compare(ItemHelperClass item1, ItemHelperClass item2) {
                            return item2.getItemName().compareToIgnoreCase(item1.getItemName());
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
                        ItemHelperClass dataClass = itemSnapshot.getValue(ItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());

                        // Extract the month from the timestamp (assuming it's stored as a valid timestamp)
                        String itemTimestamp = dataClass.getDate();
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
                        ItemHelperClass dataClass = itemSnapshot.getValue(ItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());

                        // Extract the month from the timestamp (assuming it's stored as a valid timestamp)
                        String itemTimestamp = dataClass.getDate();
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
                        ItemHelperClass dataClass = itemSnapshot.getValue(ItemHelperClass.class);
                        dataClass.setKey(itemSnapshot.getKey());

                        // Extract the month from the timestamp (assuming it's stored as a valid timestamp)
                        String itemTimestamp = dataClass.getDate();
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
            databaseReference = FirebaseDatabase.getInstance().getReference("Approved");


            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        ItemHelperClass dataClass = itemSnapshot.getValue(ItemHelperClass.class);
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




}