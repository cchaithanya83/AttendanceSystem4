package com.example.attendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class attendace_veiwer extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<YourModelClass> arrayList;
    
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendace_veiwer);


    }
}