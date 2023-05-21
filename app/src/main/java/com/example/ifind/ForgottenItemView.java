package com.example.ifind;

import static android.content.ContentValues.TAG;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ForgottenItemView extends AppCompatActivity {
    TextView item_name, item_desc, item_loc, item_date, item_time, userID;
    ImageView image_full;
    Button del_button, approve_button;
    String imageUrl = "";
    String key = "";
    private DatabaseReference toFound, logref,toForgotten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_item_view);

        item_name = findViewById(R.id.item_name);
        item_desc = findViewById(R.id.item_desc);
        item_loc = findViewById(R.id.item_loc);
        item_date = findViewById(R.id.item_date);
        item_time = findViewById(R.id.item_time);
        image_full = findViewById(R.id.image_full);
        del_button = findViewById(R.id.del_buttA);
        approve_button = findViewById(R.id.approve_buttA);
        userID = findViewById(R.id.item_foundName);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Forgotten");
        toFound = FirebaseDatabase.getInstance().getReference("Found");
        logref = FirebaseDatabase.getInstance().getReference("AdminActivityLogs");




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
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Format the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        String currentDateString = dateFormat.format(currentDate);
        String currentTimeString = timeFormat.format(currentDate);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String AdminID = String.valueOf(currentUser.getDisplayName());
        String postType = "Forgotten Item Claimed!";

        String datePosted = currentDateString;
        String timePosted = currentTimeString;


        ItemHelperClass loghelperclass = new ItemHelperClass(datePosted, timePosted, AdminID, postType);
        approve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ForgottenItemView.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        logref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                                long uploadCount = dataSnapshot.getChildrenCount();
                                if (uploadCount >= 20) {
                                    // Remove the oldest key
                                    String oldestKey = null;
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        oldestKey = childSnapshot.getKey();
                                        break; // Get the first key (oldest)
                                    }
                                    if (oldestKey != null) {
                                        logref.child(oldestKey).removeValue();
                                    }
                                }

                                toFound.child(bundle.getString("Key"))
                                        .setValue( new ItemHelperClass(name, desc, loc, date, time, imageUrl,userID_)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    logref.child(key).setValue(loghelperclass);
                                                    Toast.makeText(ForgottenItemView.this, "Item Claimed!", Toast.LENGTH_LONG).show();
                                                    //remove if you want to delete the copied record from the pending
                                                    reference.child(key).removeValue();
                                                    startActivity(new Intent(getApplicationContext(), ForgottenView.class));
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                                Toast.makeText(ForgottenItemView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                                Toast.makeText(ForgottenItemView.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


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

        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ForgottenItemView.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                logref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                                        long uploadCount = dataSnapshot.getChildrenCount();
                                        if (uploadCount >= 20) {
                                            // Remove the oldest key
                                            String oldestKey = null;
                                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                oldestKey = childSnapshot.getKey();
                                                break; // Get the first key (oldest)
                                            }
                                            if (oldestKey != null) {
                                                logref.child(oldestKey).removeValue();
                                            }
                                        }

                                        logref.child(key).setValue(new ItemHelperClass(datePosted, timePosted, AdminID, "Deleted a Forgotten Item"))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                reference.child(key).removeValue();
                                                Toast.makeText(ForgottenItemView.this, "Forgotten Item Deleted", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), ForgottenView.class));
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                                        Toast.makeText(ForgottenItemView.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error requesting connection", e);
                            }

                        });
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