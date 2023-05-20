package com.example.ifind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<ItemHelperClass> datalist;

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

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public void searchDataList(ArrayList<ItemHelperClass> searchList) {
        datalist = searchList;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemHelperClass item = datalist.get(position);
        Picasso.get().load(item.getImageURL()).into(holder.recImage);
        holder.textItemName.setText(item.getItemName());
        holder.textItemLoc.setText(item.getLocation());
        holder.textItemTime.setText(item.getDate());
        holder.textItemDate.setText(item.getTime());

        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, LostItemDetails.class);
            intent.putExtra("Image", item.getImageURL());
            intent.putExtra("Item Name", item.getItemName());
            intent.putExtra("Location", item.getLocation());
            intent.putExtra("Date", item.getDate());
            intent.putExtra("Time", item.getTime());
            intent.putExtra("Description", item.getDescription());
            intent.putExtra("userID_", item.getUserID());

            intent.putExtra("Key", item.getKey());
            context.startActivity(intent);
        });
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView recImage;
    TextView textItemName, textItemLoc, textItemDate, textItemTime, textName;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        textItemTime = itemView.findViewById(R.id.textItemtime);
        textItemDate = itemView.findViewById(R.id.textItemdate);
        textItemName = itemView.findViewById(R.id.textItemName);
        textItemLoc = itemView.findViewById(R.id.textItemLoc);
        textName = itemView.findViewById(R.id.pendingFoundName);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
