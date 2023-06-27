package com.example.attendancesystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class take_attendace extends AppCompatActivity {

    private Spinner spinner;
    private EditText editText;
    private RecyclerView recyclerView;
    private ArrayList<YourModelClass> arrayList;
    private view_student.FirestoreAdapter adapter;
    private FirebaseFirestore db;
    private String subjectWanted;
    String[] subjects = {"ooc", "java", "mes"};
    private CollectionReference collectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendace);

        spinner = findViewById(R.id.spinner2);
        editText = findViewById(R.id.editTextDate);
        recyclerView = findViewById(R.id.recyclerView);


        db = FirebaseFirestore.getInstance();

        arrayList = new ArrayList<>();
        adapter = new view_student.FirestoreAdapter();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                subjectWanted = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(take_attendace.this, subjectWanted, Toast.LENGTH_SHORT).show();
                fetchDataFromFirestore(subjectWanted);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case where no item is selected
            }
        });
    }

    private void fetchDataFromFirestore(String subjectWanted) {
        collectionRef = db.collection("Student");

        Query query = collectionRef.whereEqualTo("subject", subjectWanted);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(take_attendace.this, "sucess", Toast.LENGTH_SHORT).show();
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
        class FirestoreAdapter extends RecyclerView.Adapter<FirestoreAdapter.ViewHolder> {
            private List<YourModelClass> data = new ArrayList<>();

            public void setData(List<YourModelClass> data) {
                this.data = data;
                notifyDataSetChanged();
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                YourModelClass item = data.get(position);
                holder.username.setText(item.getUsername());
                holder.usn.setText(item.getusn());
                holder.semester.setEnabled(false);
            }

            @Override
            public int getItemCount() {
                return data.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                public TextView username;
                public TextView usn;
                public TextView semester;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    username = itemView.findViewById(R.id.username);
                    usn = itemView.findViewById(R.id.usn);
                    //semester = itemView.findViewById(R.id.semester);
                }
            }
        }
}
