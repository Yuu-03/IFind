package com.example.ifind;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifind.R;
import com.example.ifind.UserRecordHelperClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserRecordAdapterClass extends RecyclerView.Adapter<UserViewHolder> {
    private Context context;
    private List<UserRecordHelperClass> dataList;

    public UserRecordAdapterClass(Context context, List<UserRecordHelperClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdetails, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<UserRecordHelperClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserRecordHelperClass item = dataList.get(position);
        holder.FullName.setText(item.getFname());
        holder.Email.setText(item.getEmail());
        holder.Phone.setText(item.getPhone());

        // Set click listener for the call button
        holder.userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the phone number of the user
                String phoneNumber = item.getPhone();

                // Create an intent with the ACTION_DIAL action and the phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
            }
        });
    }
}

class UserViewHolder extends RecyclerView.ViewHolder {
    TextView FullName, Email, Phone;
    CardView userCard;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        FullName = itemView.findViewById(R.id.UserNameText);
        Email = itemView.findViewById(R.id.UserEmailText);
        Phone = itemView.findViewById(R.id.UserPhoneText);
        userCard = itemView.findViewById(R.id.UserCard);
    }
}
