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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class ForgottenView extends AppCompatActivity {
    List<ItemHelperClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    AdapterClass adapter;

    Spinner filterSpinner;
    SearchView searchView1;
    Button mBack;
    BottomNavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_view);

        mBack = findViewById(R.id.back);
        filterSpinner = findViewById(R.id.filter_spinner);
        searchView1 = findViewById(R.id.search);
        searchView1.clearFocus();



        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        dataList = new ArrayList<>();
        adapter = new AdapterClass(getApplicationContext(), dataList, true, false, false, false, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("Forgotten");
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

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgottenView.this, AdminMain.class));
                finish();
            }
        });



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
        databaseReference = FirebaseDatabase.getInstance().getReference("Forgotten");
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
            databaseReference = FirebaseDatabase.getInstance().getReference("Forgotten");


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