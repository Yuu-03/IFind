package com.example.ifind;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AppreciationUserView extends AppCompatActivity {
    TextView personName, itemName, Department, DateTime, Caption;
    ImageView image_full;
    String key = "";
    String imageUrl = "";
    String DateandTime = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciation_user_view);

        itemName = findViewById(R.id.AppreUserItemName);
        personName = findViewById(R.id.personNameAppreUser);
        Department = findViewById(R.id.AppreUserDepType);
        DateTime = findViewById(R.id.AppreUserDateTime);
        Caption = findViewById(R.id.AppreUserCaption);
        image_full = findViewById(R.id.image_full);


        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {

            DateandTime = bundle.getString("Date") + "  |  " + bundle.getString("Time");
            personName.setText(bundle.getString("Person Name"));
            Department.setText(bundle.getString("Department"));
            itemName.setText(bundle.getString("Item Name"));
            DateTime.setText(DateandTime);
            Caption.setText(bundle.getString("Description"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Picasso.get().load(bundle.getString("Image")).into(image_full);

        }


    }
}