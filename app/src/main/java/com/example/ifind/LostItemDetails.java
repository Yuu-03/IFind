package com.example.ifind;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LostItemDetails extends AppCompatActivity {
    TextView item_name, item_desc, item_loc, item_date, item_time, userID;

    String student_nme;
    ImageView image_full;
    Button del_button, approve_button;
    String key = "";
    String imageUrl = "";
    private DatabaseReference toPath1,logref;
    String NotifTitle, NotifMessage;
    private ItemHelperClass itemHelperClass;
    int hour1, minute1;
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
        userID = findViewById(R.id.pendingFoundName);
        item_time.setKeyListener(null);
        item_date.setKeyListener(null);

        NotifTitle = "Announcement!";
        NotifMessage = "New lost items! Check them out.";


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SubmitLostItem");
        toPath1 = FirebaseDatabase.getInstance().getReference("Approved");
        logref = FirebaseDatabase.getInstance().getReference("AdminActivityLogs");



        Bundle bundle = getIntent().getExtras();


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
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        item_date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth) {
                    month1 = month1 + 1;
                    String monthString = String.format(Locale.getDefault(), "%02d", month1); // Format month as two digits
                    String date = dayOfMonth + "/" + monthString + "/" + year1;
                    item_date.setText(date);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        item_time.setOnClickListener(v -> {
            // Get current time
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour1 = hourOfDay;
                    minute1 = minute;
                    String time = hour1 + ":" + minute1;
                    SimpleDateFormat twentyfour = new SimpleDateFormat("HH:mm");
                    Date date = null;
                    try {
                        date = twentyfour.parse(time);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    SimpleDateFormat twelvehours = new SimpleDateFormat("hh:mm aa");
                    item_time.setText(twelvehours.format(date));
                }
            }, hourOfDay, minute, false);

            // Set current time
            timePickerDialog.updateTime(hourOfDay, minute);

            timePickerDialog.show();
        });


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String AdminID = String.valueOf(currentUser.getDisplayName());


        approve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = item_name.getText().toString();
                String loc = item_loc.getText().toString();
                String desc = item_desc.getText().toString();
                String date = item_date.getText().toString();
                String time = item_time.getText().toString();
                String userID_ = userID.getText().toString();

                ItemHelperClass itemhelperClass = new ItemHelperClass(name, desc, loc, date, time, imageUrl,userID_);

                AlertDialog.Builder builder = new AlertDialog.Builder(LostItemDetails.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if ( !item_date_condition() || !item_time_condition()) {
                            return;
                        } else {
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
                                    toPath1.child(key)
                                            .setValue(itemhelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        ItemHelperClass loghelperclass = new ItemHelperClass(timestamp, AdminID, "Verified and Approved an item to lost items");

                                                        logref.child(key).setValue(loghelperclass);
                                                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/All",NotifTitle, NotifMessage, getApplicationContext(), LostItemDetails.this);
                                                        notificationsSender.SendNotifications();

                                                        Toast.makeText(LostItemDetails.this, "Item Approved!", Toast.LENGTH_LONG).show();
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

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                                    Toast.makeText(LostItemDetails.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
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
                                        ItemHelperClass loghelperclass = new ItemHelperClass(timestamp, AdminID, "Deleted a Pending Lost Item");

                                        logref.child(key).setValue(loghelperclass);
                                        reference.child(key).removeValue();
                                        Toast.makeText(LostItemDetails.this, "Item Delested", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), pendingRequests.class));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(LostItemDetails.this, "Request Cancelled", Toast.LENGTH_SHORT).show();

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

    private Boolean item_date_condition() {
        String value = item_date.getText().toString();
        if (value.isEmpty()) {
            item_date.setError("Field cannot be empty");
            return false;
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date selectedDate = format.parse(value);

            // Get current date
            Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);

            // Set the maximum date to 6 months ago
            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.MONTH, -6);
            minDate.set(Calendar.HOUR_OF_DAY, 0);
            minDate.set(Calendar.MINUTE, 0);
            minDate.set(Calendar.SECOND, 0);
            minDate.set(Calendar.MILLISECOND, 0);

            // Check if the selected date is a future date or more than 6 months ago
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTime(selectedDate);

            if (selectedCalendar.after(currentDate) || selectedCalendar.before(minDate)) {
                Toast.makeText(this, "Please select a date within the last 6 months", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Update the date picker text
            String selectedDateStr = format.format(selectedCalendar.getTime());
            item_date.setText(selectedDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }





    private Boolean item_time_condition() {
        String value = item_time.getText().toString();
        if (value.isEmpty()) {
            item_time.setError("Field cannot be empty");
            return false;
        }

        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        try {
            Date selectedTime = format.parse(value);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedTime);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

            if (hourOfDay < 6 || hourOfDay > 22) {
                item_time.setError("Please select a time between 6 AM and 10 PM");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }
}