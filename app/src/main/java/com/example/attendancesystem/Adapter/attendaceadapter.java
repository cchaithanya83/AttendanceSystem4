package com.example.attendancesystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.YourModelClass;

import java.util.ArrayList;

public class attendaceadapter extends RecyclerView.Adapter<attendaceadapter.MyViewHolder> {
    private Context context;
    private ArrayList<YourModelClass> userArrayList;

    public attendaceadapter(Context context, ArrayList<YourModelClass> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        YourModelClass user = userArrayList.get(position);
        holder.name.setText(user.getUsername());
        holder.usn.setText(user.getusn());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, usn;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameenterr);
            usn = itemView.findViewById(R.id.usn);
        }
    }
}
