package com.example.ifind;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Founditems extends AppCompatActivity {
    TextView item_name, item_desc, item_loc, item_date, item_time, userID;
    ImageView image_full;
    Button del_button1;
    String imageUrl = "";
    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founditems);
        item_name = findViewById(R.id.item_name);
        item_desc = findViewById(R.id.item_desc);
        item_loc = findViewById(R.id.item_loc);
        item_date = findViewById(R.id.item_date);
        item_time = findViewById(R.id.item_time);
        image_full = findViewById(R.id.image_full);
        del_button1 = findViewById(R.id.del_buttonFound);
        userID = findViewById(R.id.pendingFoundName_);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Found");

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("Item Name");
        String loc = bundle.getString("Location");
        String desc = bundle.getString("Description");
        String date = bundle.getString("Date");
        String time = bundle.getString("Time");
        String userID_ = bundle.getString("userID_");

        if (bundle != null) {
            item_name.setText(bundle.getString("Item Name"));
            item_loc.setText(bundle.getString("Location"));
            item_desc.setText(bundle.getString("Description"));
            item_date.setText(bundle.getString("Date"));
            item_time.setText(bundle.getString("Time"));
            userID.setText(bundle.getString("userID_"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Picasso.get().load(bundle.getString("Image")).into(image_full);
        }

        del_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Founditems.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

                        if (key != null) {
                            reference.child(key).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Founditems.this, "Record Deleted", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), FoundAdmin.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Error requesting connection", e);
                                        }
                                    });
                        } else {
                            Toast.makeText(Founditems.this, "Can't find item", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }
}
