package com.example.ifind;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class lostimageuserviewadapter extends RecyclerView.Adapter<MyViewHolder5> {
    private final Context context;
    private final List<LostImageHelperClass> datalist;


    public lostimageuserviewadapter (Context context, List<LostImageHelperClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder5 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagerecyclerviewlost, parent, false);
        return new MyViewHolder5(view);
    }


    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
//            ;
//        imageUri = data.getData();
//
//        Picasso.get().load(imageUri).into(image_preview);
//        image_preview.setImageURI(imageUri);
//    }
    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public void onBindViewHolder(@NonNull MyViewHolder5 holder, int position) {
//        Picasso.Builder builder = new Picasso.Builder(context.getApplicationContext());
//        Picasso picasso = builder.build();
//        picasso.load(datalist.get(position).getImageURL()).into(holder.recImage);
        Picasso.get().load(datalist.get(position).getImageURL()).into(holder.recImage);
//        Glide.with(context).load(datalist.get(position).getImageURL()).into(holder.recImage);
        holder.textItemName.setText(datalist.get(position).getItemName());

        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, userviewlostdetails.class);
            intent.putExtra("Image", datalist.get(holder.getAdapterPosition()).getImageURL());
            intent.putExtra("Item Name", datalist.get(holder.getAdapterPosition()).getItemName());
            intent.putExtra("Location", datalist.get(holder.getAdapterPosition()).getLocation());
            intent.putExtra("Date", datalist.get(holder.getAdapterPosition()).getDate());
            intent.putExtra("Time", datalist.get(holder.getAdapterPosition()).getTime());
            intent.putExtra("Description", datalist.get(holder.getAdapterPosition()).getDescription());
            context.startActivity(intent);

        });
    }


    public void searchDataList(ArrayList<LostImageHelperClass> searchList) {
    }
}

class MyViewHolder5 extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView textItemName;
    CardView recCard;
    public MyViewHolder5(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage2);
        recCard = itemView.findViewById(R.id.recCard2);
        textItemName = itemView.findViewById(R.id.textItemName2);
    }

}

