package com.example.ifind;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    Button submitlostbutton;
    Button viewfoundbutton;
    Activity submittingitems;
    Activity foundactivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        submittingitems = getActivity();
        foundactivity = getActivity();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        submitlostbutton = root.findViewById(R.id.submitlostbutton);
        viewfoundbutton = root.findViewById(R.id.viewlost);
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

        Button viewfoundbutton = (Button) foundactivity.findViewById(R.id.viewlost);
        viewfoundbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(foundactivity, FoundActivity.class);
                startActivity(intent);
            }
        });
    }

}