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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<MyViewHolder> {
    private final Context context;
    private final List<ItemHelperClass> datalist;

    public AdapterClass(Context context, List<ItemHelperClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Picasso.Builder builder = new Picasso.Builder(context.getApplicationContext());
//        Picasso picasso = builder.build();
//        picasso.load(datalist.get(position).getImageURL()).into(holder.recImage);
        Picasso.get().load(datalist.get(position).getImageURL()).into(holder.recImage);
//        Glide.with(context.getApplicationContext()).load(datalist.get(position).getImageURL()).into(holder.recImage);
        holder.textItemName.setText(datalist.get(position).getItemName());
        holder.textItemLoc.setText(datalist.get(position).getDescription());
        holder.textItemTime.setText(datalist.get(position).getDate());
        holder.textItemDate.setText(datalist.get(position).getTime());

        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, LostItemDetails.class);
            intent.putExtra("Image", datalist.get(holder.getAdapterPosition()).getImageURL());
            intent.putExtra("Item Name", datalist.get(holder.getAdapterPosition()).getItemName());
            intent.putExtra("Description", datalist.get(holder.getAdapterPosition()).getDescription());
            intent.putExtra("Date", datalist.get(holder.getAdapterPosition()).getDate());
            intent.putExtra("Time", datalist.get(holder.getAdapterPosition()).getTime());
            context.startActivity(intent);

        });
    }
}
class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView textItemName, textItemLoc, textItemDate, textItemTime;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage =  itemView.findViewById(R.id.recImage);
        textItemTime = itemView.findViewById(R.id.textItemtime);
        textItemDate = itemView.findViewById(R.id.textItemdate);
        textItemName = itemView.findViewById(R.id.textItemName);
        textItemLoc = itemView.findViewById(R.id.textItemLoc);
        recCard = itemView.findViewById(R.id.recCard);

    }

}

