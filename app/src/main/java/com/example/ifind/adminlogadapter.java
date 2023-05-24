package com.example.ifind;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class adminlogadapter extends RecyclerView.Adapter<MyViewHolderLog> {
    private final Context context;
    private final List<LogHelperClass> datalist;


    public adminlogadapter(Context context, List<LogHelperClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolderLog onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminlogs, parent, false);
        return new MyViewHolderLog(view);
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public void onBindViewHolder(@NonNull MyViewHolderLog holder, int position) {

        LogHelperClass item = datalist.get(position);
        String dateTime = item.getTimestamp();
        holder.adminlogname.setText(item.getUserID());
        holder.logdate.setText(dateTime);
        holder.activitytext.setText(item.getPostType());

    }
}
class MyViewHolderLog extends RecyclerView.ViewHolder {

    TextView activitytext, logdate, adminlogname;
    CardView recCard;
    public MyViewHolderLog(@NonNull View itemView) {
        super(itemView);
        recCard = itemView.findViewById(R.id.AdminCard);
        activitytext = itemView.findViewById(R.id.AdminActivityText);
        logdate = itemView.findViewById(R.id.AdminDateLog);
        adminlogname = itemView.findViewById(R.id.AdminNameText);
    }

}

