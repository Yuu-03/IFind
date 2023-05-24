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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderLog holder, int position) {
        LogHelperClass item = datalist.get(position);
        String dateTime = item.getTimestamp();

        // Convert timestamp to 12-hour format with AM and PM
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        String formattedDateTime = "";
        try {
            Date date = inputFormat.parse(dateTime);
            formattedDateTime = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.adminlogname.setText(item.getUserID());
        holder.logdate.setText(formattedDateTime);
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

