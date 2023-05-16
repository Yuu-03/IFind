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

public class AppreciationAdapterClass extends RecyclerView.Adapter<MyViewHolder7> {
    private Context context;
    private List<AppreciationItemHelperClass> datalist;


    public AppreciationAdapterClass(Context context, List<AppreciationItemHelperClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }
    @NonNull
    @Override
    public MyViewHolder7 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_itemappreciation1, parent, false);
        return new MyViewHolder7(view);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    public void searchDataList(ArrayList<AppreciationItemHelperClass> searchList){
        datalist = searchList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder7 holder, int position) {
        Picasso.get().load(datalist.get(position).getImageURL()).into(holder.recImage);
        holder.textItemName.setText(datalist.get(position).getItemName());
        holder.textItemTime.setText(datalist.get(position).getDate());
        holder.textItemDate.setText(datalist.get(position).getTime());


        holder.recCard1.setOnClickListener(view -> {
            Intent intent = new Intent(context, userviewlostdetails.class);
            intent.putExtra("Image", datalist.get(holder.getAdapterPosition()).getImageURL());
            intent.putExtra("Item Name", datalist.get(holder.getAdapterPosition()).getItemName());
            intent.putExtra("Date", datalist.get(holder.getAdapterPosition()).getDate());
            intent.putExtra("Time", datalist.get(holder.getAdapterPosition()).getTime());
            intent.putExtra("Key", datalist.get(holder.getAdapterPosition()).getKey());
            context.startActivity(intent);

        });
    }
}
class MyViewHolder7 extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView textItemName, textItemDate, textItemTime;
    CardView recCard1;

    public MyViewHolder7(@NonNull View itemView) {
        super(itemView);
        recImage =  itemView.findViewById(R.id.recImage1);
        textItemTime = itemView.findViewById(R.id.textItemtimeAppre);
        textItemDate = itemView.findViewById(R.id.textItemdateAppre);
        textItemName = itemView.findViewById(R.id.textItemNameAppre);
        recCard1 = itemView.findViewById(R.id.recCard1);

    }

}

