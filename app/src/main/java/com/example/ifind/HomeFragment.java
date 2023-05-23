package com.example.ifind;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    Button submitlostbutton;
    Button viewfoundbutton;
    Activity submittingitems;
    Activity foundactivity;

    List<LostImageHelperClass> dataList;
    List<FoundImageHelperClass> dataList2;

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        submittingitems = getActivity();
        foundactivity = getActivity();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        submitlostbutton = root.findViewById(R.id.submitlostbutton);
        return root;
    }
    public void onStart(){
        super.onStart();
        Button submitlostbutton = (Button) submittingitems.findViewById(R.id.submitlostbutton);
        submitlostbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(submittingitems, SubmittingItems.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.lostitemsrecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();

        lostimageuserviewadapter adapter = new lostimageuserviewadapter(getContext(), dataList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("Approved");
        eventListener = databaseReference.limitToFirst(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    LostImageHelperClass dataClass = itemSnapshot.getValue(LostImageHelperClass.class);
                    Collections.reverse(dataList);
                    dataList.add(dataClass);
                    Collections.reverse(dataList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView2 = view.findViewById(R.id.lostitemsrecycler2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 2);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        dataList2 = new ArrayList<>();

        FoundImageAdapter adapter2 = new FoundImageAdapter(getContext(), dataList2);

        recyclerView2.setAdapter(adapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("Found");
        eventListener = databaseReference.limitToFirst(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList2.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    FoundImageHelperClass dataClass2 = itemSnapshot.getValue(FoundImageHelperClass.class);
                    Collections.reverse(dataList2);
                    dataList2.add(dataClass2);
                    Collections.reverse(dataList);
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("All");



    }




}