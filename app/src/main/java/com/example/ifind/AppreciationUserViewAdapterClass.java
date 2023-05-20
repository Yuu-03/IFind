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

public class AppreciationUserViewAdapterClass extends RecyclerView.Adapter<MyViewHolderUserAdapter> {
    private Context context;
    private List<AppreciationItemHelperClass> datalist;


    public AppreciationUserViewAdapterClass(Context context, List<AppreciationItemHelperClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }
    @NonNull
    @Override
    public MyViewHolderUserAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_itemappreciation1, parent, false);
        return new MyViewHolderUserAdapter(view);
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
    public void onBindViewHolder(@NonNull MyViewHolderUserAdapter holder, int position) {

        Picasso.get().load(datalist.get(position).getImageURL()).into(holder.recImage);
        holder.personName.setText(datalist.get(position).getPersonName());
        holder.personType.setText(datalist.get(position).getDepartment());
        holder.datePosted.setText(datalist.get(position).getDatePosted());
        holder.timePosted.setText(datalist.get(position).getTimePosted());

        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, AppreciationUserView.class);
            intent.putExtra("Image", datalist.get(holder.getAdapterPosition()).getImageURL());
            intent.putExtra("Person Name", datalist.get(holder.getAdapterPosition()).getPersonName());
            intent.putExtra("Department", datalist.get(holder.getAdapterPosition()).getDepartment());
            intent.putExtra("Date", datalist.get(holder.getAdapterPosition()).getDatePosted());
            intent.putExtra("Time", datalist.get(holder.getAdapterPosition()).getTimePosted());
            intent.putExtra("Description", datalist.get(holder.getAdapterPosition()).getDescription());
            intent.putExtra("Item Name", datalist.get(holder.getAdapterPosition()).getItemname());

            intent.putExtra("Key", datalist.get(holder.getAdapterPosition()).getKey());
            context.startActivity(intent);

        });
    }
}
class MyViewHolderUserAdapter extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView personName, personType, datePosted, timePosted;
    CardView recCard;

    public MyViewHolderUserAdapter(@NonNull View itemView) {
        super(itemView);
        recImage =  itemView.findViewById(R.id.recImage1);
        recCard = itemView.findViewById(R.id.recCard1);
        personName = itemView.findViewById(R.id.postNameAppre);
        personType = itemView.findViewById(R.id.postTypeAppre);
        datePosted = itemView.findViewById(R.id.postDateAppre);
        timePosted = itemView.findViewById(R.id.postTimeAppre);


    }

}

