package com.example.ifind;

import android.annotation.SuppressLint;
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

public class FoundAdapterClass extends RecyclerView.Adapter<MyViewHolder9> {
    private Context context;
    private List<ItemHelperClass> datalist;


    public FoundAdapterClass(Context context, List<ItemHelperClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }
    @NonNull
    @Override
    public MyViewHolder9 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder9(view);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    public void searchDataList(ArrayList<ItemHelperClass> searchList){
        datalist = searchList;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder9 holder, int position) {
        ItemHelperClass item = datalist.get(position);
        Picasso.get().load(item.getImageURL()).into(holder.recImage);
        holder.textItemName.setText(item.getItemName());
        holder.textItemLoc.setText(item.getLocation());
        holder.textItemTime.setText(item.getDate());
        holder.textItemDate.setText(item.getTime());



        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, Founditems.class);
            intent.putExtra("Image", item.getImageURL());
            intent.putExtra("Item Name", item.getItemName());
            intent.putExtra("Location", item.getLocation());
            intent.putExtra("Date", item.getDate());
            intent.putExtra("Time", item.getTime());
            intent.putExtra("Description", item.getDescription());
            intent.putExtra("Key", item.getKey());
            intent.putExtra("userID_", item.getUserID());
            context.startActivity(intent);
        });
    }

}
class MyViewHolder9 extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView textItemName, textItemLoc, textItemDate, textItemTime;
    CardView recCard;

    public MyViewHolder9(@NonNull View itemView) {
        super(itemView);
        recImage =  itemView.findViewById(R.id.recImage);
        textItemTime = itemView.findViewById(R.id.textItemtime);
        textItemDate = itemView.findViewById(R.id.textItemdate);
        textItemName = itemView.findViewById(R.id.textItemName);
        textItemLoc = itemView.findViewById(R.id.textItemLoc);
        recCard = itemView.findViewById(R.id.recCard);

    }

}

