package com.example.ifind;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class LostItemDetails extends AppCompatActivity {
     TextView item_name, item_desc, item_loc, item_date, item_time;
     ImageView image_full;
     Button del_button;
     String key = "";
     String imageUrl = "";
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

        Bundle bundle = getIntent().getExtras();
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
//    del_button.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SubmitLostItem");
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference storageReference =  storage.getReferenceFromUrl(imageUrl);
//            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    reference.child(key).removeValue();
//                    Snackbar.make(findViewById(android.R.id.content), "Item Deleted", Snackbar.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                }
//            });
//        }
//    });
    }
}