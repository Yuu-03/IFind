package com.example.ifind;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

public class LostItemDetails extends AppCompatActivity {
    TextView item_name, item_desc, item_loc, item_date, item_time;

    EditText student_name;
    String student_nme;
    ImageView image_full;
    Button del_button, approve_button;
    String key = "";
    String imageUrl = "";
    private DatabaseReference toPath, toPathAppr;
    private ItemHelperClass itemHelperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_details);

        item_name = findViewById(R.id.item_name);
        item_desc = findViewById(R.id.item_desc);
        item_loc = findViewById(R.id.item_loc);
        item_date = findViewById(R.id.item_date);
        item_time = findViewById(R.id.item_time);
        image_full = findViewById(R.id.image_full);
        del_button = findViewById(R.id.del_button);
        approve_button = findViewById(R.id.approve_butt);
        student_name = findViewById(R.id.Student_namee);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SubmitLostItem");
        toPath = FirebaseDatabase.getInstance().getReference("Approved");


        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("Item Name");
        String loc = bundle.getString("Location");
        String desc = bundle.getString("Description");
        String date = bundle.getString("Date");
        String time = bundle.getString("Time");
        student_nme = student_name.getText().toString();


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

                AlertDialog.Builder builder = new AlertDialog.Builder(LostItemDetails.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        toPath = FirebaseDatabase.getInstance().getReference("Approved");
                        toPathAppr =  FirebaseDatabase.getInstance().getReference("Appreciate");

                        ItemHelperClass itemhelperClass = new ItemHelperClass(name, desc, loc, date, time, imageUrl);

                        toPath.child(key)
                                .setValue(itemhelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            toPathAppr.child(key).setValue(new ItemHelperClass(name, desc, loc, date, time, imageUrl));
                                            toPathAppr.child(key).setValue(student_nme);
                                            Toast.makeText(LostItemDetails.this, "Approved! Displayed in Lost Items!", Toast.LENGTH_LONG).show();
                                            //remove if you want to delete the copied record from the pending
                                            reference.child(key).removeValue();
                                            startActivity(new Intent(getApplicationContext(), pendingRequests.class));
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                                        Toast.makeText(LostItemDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

                AlertDialog.Builder builder = new AlertDialog.Builder(LostItemDetails.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                reference.child(key).removeValue();
                                Toast.makeText(LostItemDetails.this, "Request Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), pendingRequests.class));
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