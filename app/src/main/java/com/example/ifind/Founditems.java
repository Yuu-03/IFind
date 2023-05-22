package com.example.ifind;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        ItemHelperClass loghelperclass = new ItemHelperClass(datePosted, timePosted, AdminID,"Deleted a an item in found");


        del_button.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(Founditems.this);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", (dialog, which) -> {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

                reference.child(key).removeValue().addOnSuccessListener(aVoid -> {
                    logref.child(key).setValue(loghelperclass);
                    storageReference.delete();

                    Toast.makeText(Founditems.this, "Post Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Founditems.this, FoundAdmin.class));
                }).addOnFailureListener(e -> Log.e(TAG, "Error requesting connection", e));
            });

            builder.setNegativeButton("NO", (dialog, which) -> {

                // Do nothing
                dialog.dismiss();
            });

            AlertDialog alert = builder.create();
            alert.show();

        });

    }
}
