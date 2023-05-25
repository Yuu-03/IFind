package com.example.ifind;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class ApprovedItemDetails extends AppCompatActivity {
    TextView item_name, item_desc, item_loc, item_date, item_time, userID;
    ImageView image_full;
    Button del_button, approve_button, edit_button;
    String imageUrl = "";
    String key = "";

    private DatabaseReference toFound, logref,toForgotten;

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
        userID = findViewById(R.id.item_foundName);
        edit_button = findViewById(R.id.EditButton);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Approved");
        toFound = FirebaseDatabase.getInstance().getReference("Found");
        toForgotten = FirebaseDatabase.getInstance().getReference("Forgotten");
        logref = FirebaseDatabase.getInstance().getReference("AdminActivityLogs");
        item_time.setKeyListener(null);
        item_date.setKeyListener(null);

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

        // Format the date and time

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String AdminID = String.valueOf(currentUser.getDisplayName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());


        ItemHelperClass loghelperclass1 = new ItemHelperClass(timestamp, AdminID,"An owner claimed an item");
        ItemHelperClass loghelperclass2 = new ItemHelperClass(timestamp, AdminID,"Moved a lost item from forgotten box");

        approve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovedItemDetails.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        logref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                                long uploadCount = dataSnapshot.getChildrenCount();
                                if (uploadCount >= 50) {
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
                                                    // Notification
                                                    logref.child(key).setValue(loghelperclass1);
                                                    Toast.makeText(ApprovedItemDetails.this, "Approved! Displayed in Lost Items!", Toast.LENGTH_LONG).show();
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

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                                Toast.makeText(ApprovedItemDetails.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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

                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovedItemDetails.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        logref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                                long uploadCount = dataSnapshot.getChildrenCount();
                                if (uploadCount >= 50) {
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

                                toForgotten.child(bundle.getString("Key"))
                                        .setValue( new ItemHelperClass(name, desc, loc, date, time, imageUrl,userID_)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ApprovedItemDetails.this, "Item Moved to Forgotten Box!", Toast.LENGTH_LONG).show();
                                                    logref.child(key).setValue(loghelperclass2);
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

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                                Toast.makeText(ApprovedItemDetails.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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
        image_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup(imageUrl);
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApprovedItemDetails.this, LostItemEdit.class);
                intent.putExtra("Item Name", name);
                intent.putExtra("Location", loc);
                intent.putExtra("Description", desc);
                intent.putExtra("Date", date);
                intent.putExtra("Time", time);
                intent.putExtra("userID_", userID_);
                intent.putExtra("Key", key);
                intent.putExtra("Image", imageUrl);
                startActivity(intent);

            }
        });

    }
    private void showImagePopup(String imageUrl) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_full_size_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        ImageView zoomedImageView = dialog.findViewById(R.id.zoomed_image);
        Picasso.get().load(imageUrl).into(zoomedImageView);

        // Add click listener to the dialog background to exit the zoomed-in view
        dialog.findViewById(R.id.dialog_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}