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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Date;

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

        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AppreciationAdminView.this);

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
                                Toast.makeText(AppreciationAdminView.this, "Post Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AppreciationAdminView.this, AdminAppreciate.class));
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