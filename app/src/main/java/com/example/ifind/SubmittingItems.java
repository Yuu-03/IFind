package com.example.ifind;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SubmittingItems extends AppCompatActivity {
    private FirebaseAuth mAuth;
    int hour1, minute1;
    private TextInputLayout itemname, itemlocation, description;
    private TextInputEditText date_picker, time_picker;
    private Uri imageUri;
    private ImageView image_preview;
    private StorageReference storageRef;
    private DatabaseReference databaseRef, logref;
    private DatabaseReference userRef;
    String imageURL;
    String image_path_firebase;
    private static final int PICK_IMAGE_REQUEST = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_submitting_items);

        storageRef = FirebaseStorage.getInstance().getReference("LostItemImage");
        logref = FirebaseDatabase.getInstance().getReference("UserActivityLogs");
        databaseRef = FirebaseDatabase.getInstance().getReference("LostItemImage");
        Button mupload = findViewById(R.id.upload);

        date_picker = findViewById(R.id.date_picker);
        time_picker = findViewById(R.id.time_picker);
        itemname = findViewById(R.id.itemname);
        itemlocation = findViewById(R.id.itemlocation);
        description = findViewById(R.id.description);
        Button submit_post = findViewById(R.id.submit_post);
        image_preview = findViewById(R.id.image_preview);
        date_picker.setKeyListener(null);
        time_picker.setKeyListener(null);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        mupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item_name_condition() || !item_loc_condition() || !item_date_condition() || !item_time_condition() || !item_description_condition()) {
                    // Conditions are not met, display an error message
                    Toast.makeText(SubmittingItems.this, "Please fill in all required fields correctly", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubmittingItems.this);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            uploadPicture();
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
            }
        });


        date_picker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth) {
                    month1 = month1 + 1;
                    String monthString = String.format(Locale.getDefault(), "%02d", month1);
                    String dayString = String.format(Locale.getDefault(), "%02d", dayOfMonth);// Format month as two digits
                    String date = dayString + "/" + monthString + "/" + year1;
                    date_picker.setText(date);
                }
            }, year, month, day);
            datePickerDialog.show();
        });


        time_picker.setOnClickListener(v -> {
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
                            time_picker.setText(twelvehours.format(date));
                        }
                    }, hourOfDay, minute, false);

            // Set current time
            timePickerDialog.updateTime(hourOfDay, minute);

            timePickerDialog.show();
        });



    }

    private void openFileChooser() {
        Intent photoPicker = new Intent();
        photoPicker.setType("image/*");
        photoPicker.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPicker, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(image_preview);
            image_preview.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver conres = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(conres.getType(uri));
    }

    private Boolean item_name_condition() {
        String value  = itemname.getEditText().getText().toString();
        if (value.isEmpty()) {
            itemname.setError("Field cannot be empty");
            return false;
        } else {
            itemname.setError(null);
            itemname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean item_loc_condition() {
        String value  = itemlocation.getEditText().getText().toString();
        if (value.isEmpty()) {
            itemlocation.setError("Field cannot be empty");
            return false;
        } else {
            itemlocation.setError(null);
            itemlocation.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean item_date_condition() {
        String value = date_picker.getText().toString();
        if (value.isEmpty()) {
            date_picker.setError("Field cannot be empty");
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
            date_picker.setText(selectedDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }





    private Boolean item_time_condition() {
        String value = time_picker.getText().toString();
        if (value.isEmpty()) {
            time_picker.setError("Field cannot be empty");
            return false;
        }

        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        try {
            Date selectedTime = format.parse(value);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedTime);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

            if (hourOfDay < 6 || hourOfDay > 22) {
                time_picker.setError("Please select a time between 6 AM and 10 PM");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }


    private Boolean item_description_condition() {
        String value  = description.getEditText().getText().toString();
        if (value.isEmpty()) {
            description.setError("Field cannot be empty");
            return false;
        } else {
            description.setError(null);
            description.setErrorEnabled(false);
            return true;
        }
    }
    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        // Create a reference to "mountains.jpg"
        if (imageUri != null) {
            pd.setTitle("Uploading the image...");
            pd.show();

            StorageReference fileReference = storageRef.child(String.valueOf(imageUri));
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            imageURL = imageUri.toString();
                            Task <Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            uriTask
                                    .addOnSuccessListener(result -> {
                                        Uri urlImage = uriTask.getResult();
                                        imageURL = urlImage.toString();
                                        uploadItemInformation();
                                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Snackbar.make(findViewById(android.R.id.content), "Failed", Snackbar.LENGTH_LONG).show();
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
                        }

                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percentage: " + (int) progressPercent + "%");
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadItemInformation() {
        // conditions
        databaseRef = FirebaseDatabase.getInstance().getReference("SubmitLostItem");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        if (!item_name_condition() | !item_loc_condition() | !item_date_condition()| !item_time_condition()| !item_description_condition()) {
            return;
        }

        //get the values from the fields
        String itemName = itemname.getEditText().getText().toString();
        String itemDescription = description.getEditText().getText().toString();
        String itemLocation = itemlocation.getEditText().getText().toString();
        String dateFound = date_picker.getText().toString();
        String timeFound = time_picker.getText().toString();
        String userID = String.valueOf(currentUser.getDisplayName());

        String postType = "Posted Lost Item";

        //call the class UserHelperClass to use and store values to the database
        ItemHelperClass ItemhelperClass = new ItemHelperClass(itemName, itemDescription, itemLocation, dateFound, timeFound, imageURL, userID);
        ItemHelperClass loghelperclass = new ItemHelperClass(timestamp, userID, postType);
        //assign an Id to add more users
        String uploadID = databaseRef.push().getKey();


        logref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                databaseRef.child(uploadID)
                        .setValue(ItemhelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            String NotifTitle = "Hi Admin";
                            String NotifMessage = "New Pending Items";
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    logref.child(uploadID).setValue(loghelperclass);

                                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/Admin",NotifTitle, NotifMessage, getApplicationContext(), SubmittingItems.this);
                                    notificationsSender.SendNotifications();


                                    Toast.makeText(SubmittingItems.this,"Item Information Uploaded",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SubmittingItems.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SubmittingItems.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
