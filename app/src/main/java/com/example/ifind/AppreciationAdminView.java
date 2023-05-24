package com.example.ifind;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

public class AppreciationAdminView extends AppCompatActivity {
    TextView personName, itemName, Department, DateTime, Caption;
    ImageView image_full;
    Button del_button;
    String key = "";
    String imageUrl = "";
    String DateandTime = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciation_admin_view);

        itemName = findViewById(R.id.AppreUserItemName);
        personName = findViewById(R.id.personNameAppreUser);
        Department = findViewById(R.id.AppreUserDepType);
        DateTime = findViewById(R.id.AppreUserDateTime);
        Caption = findViewById(R.id.AppreUserCaption);
        image_full = findViewById(R.id.image_full);
        del_button = findViewById(R.id.deleteButt);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AppreciationPost");
        DatabaseReference logref = FirebaseDatabase.getInstance().getReference("AdminActivityLogs");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String AdminID = String.valueOf(currentUser.getDisplayName());
        String postType = "Delete an Appreciation Post";


        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AppreciationAdminView.this);

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
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ItemHelperClass loghelperclass = new ItemHelperClass(timestamp, AdminID, postType);
                                        logref.child(key).setValue(loghelperclass);
                                        reference.child(key).removeValue();
                                        Toast.makeText(AppreciationAdminView.this, "Post Deleted!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AppreciationAdminView.this, AdminAppreciate.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Error requesting connection", e);
                                    }

                                });

                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                Toast.makeText(AppreciationAdminView.this, "Error", Toast.LENGTH_SHORT).show();


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