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

public class ApprovedItemDetails extends AppCompatActivity {
    TextView item_name, item_desc, item_loc, item_date, item_time;
    ImageView image_full;
    Button del_button, approve_button;
    String key = "";
    String imageUrl = "";
    private DatabaseReference toPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_item_details);

        item_name = findViewById(R.id.item_name);
        item_desc = findViewById(R.id.item_desc);
        item_loc = findViewById(R.id.item_loc);
        item_date = findViewById(R.id.item_date);
        item_time = findViewById(R.id.item_time);
        image_full = findViewById(R.id.image_full);
        del_button = findViewById(R.id.del_buttA);
        approve_button = findViewById(R.id.approve_buttA);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Approved");
        toPath = FirebaseDatabase.getInstance().getReference("Found");


        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("Item Name");
        String loc = bundle.getString("Location");
        String desc = bundle.getString("Description");
        String date = bundle.getString("Date");
        String time = bundle.getString("Time");

        if (bundle != null) {
            item_name.setText(bundle.getString("Item Name"));
            item_loc.setText(bundle.getString("Location"));
            item_desc.setText(bundle.getString("Description"));
            item_date.setText(bundle.getString("Date"));
            item_time.setText(bundle.getString("Time"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Picasso.get().load(bundle.getString("Image")).into(image_full);
        }

        approve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toPath = FirebaseDatabase.getInstance().getReference("Found");

                ItemHelperClass ItemhelperClass = new ItemHelperClass(name, desc, loc, date, time, imageUrl);

                toPath.child(key)
                        .setValue(ItemhelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ApprovedItemDetails.this, "Found!", Toast.LENGTH_LONG).show();
                                    //remove if you want to delete the copied record from the pending
                                    reference.child(key).removeValue();
                                    startActivity(new Intent(getApplicationContext(), ApprovedAdmin.class));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                Toast.makeText(ApprovedItemDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete();
                reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ApprovedItemDetails.this, "Request Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ApprovedAdmin.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error requesting connection", e);
                    }

                });
            }
        });

    }
}