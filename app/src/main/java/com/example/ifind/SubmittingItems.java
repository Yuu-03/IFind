package com.example.ifind;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SubmittingItems extends AppCompatActivity {
    private FirebaseAuth mAuth;
    int hour1, minute1;
    private TextInputLayout itemname, itemlocation, description;
    private TextInputEditText date_picker, time_picker;
    private Uri imageUri;
    private ImageView image_preview;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private FirebaseDatabase rootNode;
    String imageURL;
    private static final int PICK_IMAGE_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_submitting_items);

        storageRef = FirebaseStorage.getInstance().getReference("LostItemImage");
        databaseRef = FirebaseDatabase.getInstance().getReference("LostItemImage");

        Button mupload = findViewById(R.id.upload);

        date_picker = findViewById(R.id.date_picker);
        time_picker = findViewById(R.id.time_picker);
        itemname = findViewById(R.id.itemname);
        itemlocation = findViewById(R.id.itemlocation);
        description = findViewById(R.id.description);
        Button submit_post = findViewById(R.id.submit_post);
        image_preview = findViewById(R.id.image_preview);

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
                rootNode = FirebaseDatabase.getInstance();
                databaseRef = rootNode.getReference("SubmitLostItem");
                //upload selected pic to database
                uploadPicture();

            }
        });
        date_picker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth) {
                    month1 = month1 + 1;
                    String date = dayOfMonth + "/" + month1 + "/" + year1;
                    date_picker.setText(date);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        time_picker.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view12, int hourOfDay, int minute) {
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
            }, 12, 0, false);
            timePickerDialog.updateTime(hour1, minute1);
            timePickerDialog.show();
        });
//        mAuth.signInAnonymously()
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(SubmittingItems.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//                    }
//                });

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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
            ;
        imageUri = data.getData();

        Picasso.get().load(imageUri).into(image_preview);
        image_preview.setImageURI(imageUri);
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
        String value  = date_picker.getText().toString();
        if (value.isEmpty()) {
            date_picker.setError("Field cannot be empty");
            return false;
        }
        return true;
    }

    private Boolean item_time_condition() {
        String value  = time_picker.getText().toString();
        if (value.isEmpty()) {
            time_picker.setError("Field cannot be empty");
            return false;
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
        final String randomKey = UUID.randomUUID().toString();
        // Create a reference to "mountains.jpg"
        if (imageUri != null) {
            pd.setTitle("Uploading the image...");
            pd.show();
            StorageReference fileReference = storageRef.child(itemname.toString()+"_"+System.currentTimeMillis());
            fileReference.putFile(imageUri)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            imageURL = imageUri.toString();
                            uploadItemInformation();

                            Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
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
        if (!item_name_condition() | !item_loc_condition() | !item_date_condition()| !item_time_condition()| !item_description_condition()) {
            return;
        }

        //get the values from the fields
        String itemName = itemname.getEditText().getText().toString();
        String itemDescription = description.getEditText().getText().toString();
        String itemLocation = itemlocation.getEditText().getText().toString();
        String dateFound = date_picker.getText().toString();
        String timeFound = time_picker.getText().toString();

        //call the class UserHelperClass to use and store values to the database
        ItemHelperClass ItemhelperClass = new ItemHelperClass(itemName, itemDescription, itemLocation, dateFound, timeFound, imageURL);

        //assign an Id to add more users
        String uploadID = databaseRef.push().getKey();

        databaseRef.child(uploadID)
                .setValue(ItemhelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
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
//    private void updateUI(FirebaseUser user) {
//
//        if (user != null) {
//            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//        }
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//


}
