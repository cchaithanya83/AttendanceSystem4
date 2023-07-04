package com.example.attendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.util.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class attendace_veiwer extends AppCompatActivity {


    public Button select;
    private RecyclerView recyclerView;
    private FirestoreAdapter adapter;

    private FirebaseFirestore db;
    private CollectionReference collectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendace_veiwer);


        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection("Student"); // Replace with your collection name
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        select=findViewById(R.id.click);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter = new FirestoreAdapter();
                recyclerView.setAdapter(adapter);

                fetchDataFromFirestore();


            }
        });


        // Fetch data from Firestore

    }

    private void fetchDataFromFirestore() {
        EditText usn1 = (EditText) findViewById(R.id.usn1);
            String usn = usn1.getText().toString();
        CollectionReference studentCollection = db.collection("Student");
        Query query = studentCollection.whereEqualTo("usn", usn);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        // Retrieve the document ID
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        DocumentReference dateDocument = studentCollection.document(documentId);
                        CollectionReference studentCollection = dateDocument.collection("ATTENDACE");
                        studentCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(attendace_veiwer.this, "sucess", Toast.LENGTH_SHORT).show();
                                    List<YourModelClass> data = new ArrayList<>();
                                    for (DocumentSnapshot document : task.getResult()) {
                                        // Assuming you have a custom model class to represent your data
                                        YourModelClass model = document.toObject(YourModelClass.class);
                                        data.add(model);
                                    }
                                    adapter.setData(data);

                                } else {
                                    Log.d("Firestore", "Error getting documents: " + task.getException());
                                }
                            }
                        });
                    }


                }
            }
        });
    }

    static class FirestoreAdapter extends RecyclerView.Adapter<FirestoreAdapter.ViewHolder> {
        private List<YourModelClass> data = new ArrayList<>();

        public void setData(List<YourModelClass> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            YourModelClass item = data.get(position);
            Timestamp timestamp = item.gettimestamp();
            if (timestamp != null) {
                Date date = timestamp.toDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

// Format the date and time as a string
                String formattedDateTime = sdf.format(date);

                holder.time.setText(formattedDateTime);

                // Perform further operations with the Date object
                // Set it to the TextView or format it as needed
            } else {
                // Handle the case where the timestamp is null
                // You can set a default value or show an error message
            }


// Create a SimpleDateFormat to format the date and time

            holder.status.setText(item.getattendanceStatus());


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView time;
            public TextView status;





            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                time = itemView.findViewById(R.id.time);
                status = itemView.findViewById(R.id.atte);


            }
        }
    }

}