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

import java.util.List;

public class FoundImageAdapter extends RecyclerView.Adapter<MyViewHolder2> {
    private final Context context;
    private final List<FoundImageHelperClass> datalist;


    public FoundImageAdapter (Context context, List<FoundImageHelperClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagerecyclerview, parent, false);
        return new MyViewHolder2(view);
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

    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
//        Picasso.Builder builder = new Picasso.Builder(context.getApplicationContext());
//        Picasso picasso = builder.build();
//        picasso.load(datalist.get(position).getImageURL()).into(holder.recImage);
        Picasso.get().load(datalist.get(position).getImageURL()).into(holder.recImage);
//        Glide.with(context).load(datalist.get(position).getImageURL()).into(holder.recImage);
        holder.textItemName.setText(datalist.get(position).getItemName());


        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, LostItemDetails.class);
            intent.putExtra("Image", datalist.get(holder.getAdapterPosition()).getImageURL());
            intent.putExtra("Item Name", datalist.get(holder.getAdapterPosition()).getItemName());
            intent.putExtra("Location", datalist.get(holder.getAdapterPosition()).getLocation());
            intent.putExtra("Date", datalist.get(holder.getAdapterPosition()).getDate());
            intent.putExtra("Time", datalist.get(holder.getAdapterPosition()).getTime());
            intent.putExtra("Description", datalist.get(holder.getAdapterPosition()).getDescription());
            context.startActivity(intent);

        });
    }
}

class MyViewHolder2 extends RecyclerView.ViewHolder {

    ImageView recImage;
    CardView recCard;
    TextView textItemName;
    public MyViewHolder2(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage1);
        recCard = itemView.findViewById(R.id.recCard1);
        textItemName = itemView.findViewById(R.id.textItemName);

    }

}

