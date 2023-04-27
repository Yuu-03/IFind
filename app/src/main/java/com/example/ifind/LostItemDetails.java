package com.example.ifind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class LostItemDetails extends AppCompatActivity {
     TextView item_name, item_desc, item_loc, item_date, item_time;
     ImageView image_full;
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            item_name.setText(bundle.getString("Item Name"));
            item_loc.setText(bundle.getString("Location"));
            item_desc.setText(bundle.getString("Description"));
            item_date.setText(bundle.getString("Date"));
            item_time.setText(bundle.getString("Time"));
            Glide.with(this).load(bundle.getString("Image")).into(image_full);
        }

    }
}