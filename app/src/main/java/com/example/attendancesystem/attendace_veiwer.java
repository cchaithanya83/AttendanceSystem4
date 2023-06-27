package com.example.attendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.attendancesystem.Adapter.attendaceadapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class attendace_veiwer extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<YourModelClass> arrayList;
    attendaceadapter myAdpater;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendace_veiwer);


    }
}