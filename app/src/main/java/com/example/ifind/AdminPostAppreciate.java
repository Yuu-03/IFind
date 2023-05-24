package com.example.ifind;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminPostAppreciate extends AppCompatActivity {
    private TextInputLayout itemname, description, personname;
    Spinner personType;
    private Uri imageUri;
    private ImageView image_preview;
    private StorageReference storageRef;
    private DatabaseReference databaseRef,logref;

    String imageURL;
    String selected = "";
    private static final int PICK_IMAGE_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post_appreciate);

        storageRef = FirebaseStorage.getInstance().getReference("AppreciationImage");
        databaseRef = FirebaseDatabase.getInstance().getReference("AppreciationPost");
        logref = FirebaseDatabase.getInstance().getReference("AdminActivityLogs");


        Button mupload = findViewById(R.id.upload);

        itemname = findViewById(R.id.item_found);
        personType = findViewById(R.id.spinnerType);
        description = findViewById(R.id.description);
        Button submit_post = findViewById(R.id.submit_appre);
        image_preview = findViewById(R.id.image_preview);
        personname = findViewById(R.id.person_name);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        personType.setAdapter(adapter);


        personType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                // Use the selected item as needed
                selected = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected
            }
        });

        mupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload selected pic to database

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPostAppreciate.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (!itemname_bool()| !description_bool()| !personname_bool()| !department_bool()) {
                            return;
                        } else {
                            uploadPicture();

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


    private Boolean department_bool() {
        String value  = selected;
        if (value.isEmpty()) {
            Toast.makeText(this, "please select type", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private Boolean itemname_bool() {
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
    private Boolean personname_bool() {
        String value  = personname.getEditText().getText().toString();
        if (value.isEmpty()) {
            personname.setError("Field cannot be empty");
            return false;
        } else {
            personname.setError(null);
            personname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean description_bool() {
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
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Format the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        String currentDateString = dateFormat.format(currentDate);
        String currentTimeString = timeFormat.format(currentDate);
        // conditions
        databaseRef = FirebaseDatabase.getInstance().getReference("AppreciationPost");

        if (!itemname_bool()| !description_bool()| !personname_bool()| !department_bool()) {
            return;
        }


        //get the values from the fields
        String Itemname = itemname.getEditText().getText().toString();
        String Description = description.getEditText().getText().toString();
        String Department = selected;
        String datePosted = currentDateString;
        String timePosted = currentTimeString;
        String PersonName = personname.getEditText().getText().toString();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String AdminID = String.valueOf(currentUser.getDisplayName());
        String postType = "Posted an Appreciation";

        //call the class UserHelperClass to use and store values to the database
        AppreciationItemHelperClass AppreciationItemHelperClass = new AppreciationItemHelperClass(Itemname, Description, Department,datePosted,timePosted, PersonName, imageURL);

        //assign an Id to add more users
        String uploadID = databaseRef.push().getKey();


        ItemHelperClass loghelperclass = new ItemHelperClass(timestamp, AdminID, postType);
        //assign an Id to add more users

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
                        .setValue(AppreciationItemHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    String NotifTitle = "Announcement";
                                    String NotifMessage = "New Appreciation Uploads! Check them out!";
                                    logref.child(uploadID).setValue(loghelperclass);
                                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/All",NotifTitle, NotifMessage, getApplicationContext(),AdminPostAppreciate.this);
                                    notificationsSender.SendNotifications();
                                    Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPostAppreciate.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
