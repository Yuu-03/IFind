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

public class ApprovedAdapterClass extends RecyclerView.Adapter<MyViewHolder4> {
    private Context context;
    private List<ItemHelperClass> datalist;


    public ApprovedAdapterClass(Context context, List<ItemHelperClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }
    @NonNull
    @Override
    public MyViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder4(view);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    public void searchDataList(ArrayList<ItemHelperClass> searchList){
        datalist = searchList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder4 holder, int position) {
        Picasso.get().load(datalist.get(position).getImageURL()).into(holder.recImage);
        holder.textItemName.setText(datalist.get(position).getItemName());
        holder.textItemLoc.setText(datalist.get(position).getLocation());
        holder.textItemTime.setText(datalist.get(position).getDate());
        holder.textItemDate.setText(datalist.get(position).getTime());


        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, ApprovedItemDetails.class);
            intent.putExtra("Image", datalist.get(holder.getAdapterPosition()).getImageURL());
            intent.putExtra("Item Name", datalist.get(holder.getAdapterPosition()).getItemName());
            intent.putExtra("Location", datalist.get(holder.getAdapterPosition()).getLocation());
            intent.putExtra("Date", datalist.get(holder.getAdapterPosition()).getDate());
            intent.putExtra("Time", datalist.get(holder.getAdapterPosition()).getTime());
            intent.putExtra("Description", datalist.get(holder.getAdapterPosition()).getDescription());
            intent.putExtra("userID_", datalist.get(holder.getAdapterPosition()).getUserID());
            intent.putExtra("Key", datalist.get(holder.getAdapterPosition()).getKey());
            context.startActivity(intent);

        });
    }
}
class MyViewHolder4 extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView textItemName, textItemLoc, textItemDate, textItemTime;
    CardView recCard;

    public MyViewHolder4(@NonNull View itemView) {
        super(itemView);
        recImage =  itemView.findViewById(R.id.recImage);
        textItemTime = itemView.findViewById(R.id.textItemtime);
        textItemDate = itemView.findViewById(R.id.textItemdate);
        textItemName = itemView.findViewById(R.id.textItemName);
        textItemLoc = itemView.findViewById(R.id.textItemLoc);
        recCard = itemView.findViewById(R.id.recCard);

    }

}

