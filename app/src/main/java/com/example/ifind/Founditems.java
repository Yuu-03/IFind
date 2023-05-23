package com.example.ifind;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class Founditems extends AppCompatActivity {
    TextView item_name, item_desc, item_loc, item_date, item_time, userID;
    ImageView image_full;
    Button del_button;
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
        del_button = findViewById(R.id.del_buttonFound);
        userID = findViewById(R.id.pendingFoundName_);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Found");
        DatabaseReference logref = FirebaseDatabase.getInstance().getReference("AdminActivityLogs");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            item_name.setText(bundle.getString("Item Name"));
            item_loc.setText(bundle.getString("Location"));
            item_desc.setText(bundle.getString("Description"));
            item_date.setText(bundle.getString("Date"));
            item_time.setText(bundle.getString("Time"));
            userID.setText(bundle.getString("userID_"));
            imageUrl = bundle.getString("Image");
            key = bundle.getString("Key");
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

        String datePosted = currentDateString;
        String timePosted = currentTimeString;

        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Founditems.this);

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
                                        ItemHelperClass loghelperclass = new ItemHelperClass(datePosted, timePosted, AdminID, "Deleted a Found Item");

                                        logref.child(key).setValue(loghelperclass);
                                        reference.child(key).removeValue();
                                        Toast.makeText(Founditems.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), pendingRequests.class));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Founditems.this, "Request Cancelled", Toast.LENGTH_SHORT).show();

                                    }
                                });
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
