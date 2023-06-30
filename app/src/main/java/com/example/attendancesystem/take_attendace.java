package com.example.attendancesystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class take_attendace extends AppCompatActivity {

    private FirestoreAdapter adapter1;
    private Spinner spinner;
    private EditText date;
    private RecyclerView recyclerView;
    private ArrayList<YourModelClass> arrayList;
    private FirebaseFirestore db;
    private String subjectWanted;
    String[] subjects = {"ooc", "java", "mes"};
    private CollectionReference collectionRef;
    String dates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendace);

        spinner = findViewById(R.id.spinner2);
        date = findViewById(R.id.editTextDate);
        date.setText("");
       // dates=date.getText().toString();


        recyclerView = findViewById(R.id.recyclerView2);

        db = FirebaseFirestore.getInstance();

        arrayList = new ArrayList<>();
        adapter1 = new FirestoreAdapter();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter1);

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
                    Toast.makeText(take_attendace.this, "Success", Toast.LENGTH_SHORT).show();
                    List<YourModelClass> data = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Assuming you have a custom model class to represent your data
                        YourModelClass model = document.toObject(YourModelClass.class);
                        data.add(model);
                    }
                    adapter1.setData(data);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            YourModelClass item = data.get(position);
            holder.username.setText(item.getUsername());
            holder.usn.setText(item.getusn());
            holder.radioGroupAttendance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    String attendanceStatus = "";
                    switch (checkedId) {
                        case R.id.radioButtonPresent:
                            attendanceStatus = "Present";
                            break;
                        case R.id.radioButtonAbsent:
                            attendanceStatus = "Absent";
                            break;
                    }

                    updateAttendanceStatus(item.getusn(), item.getUsername(), attendanceStatus);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView username;
            public TextView usn;
            public RadioGroup radioGroupAttendance;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                username = itemView.findViewById(R.id.username2);
                usn = itemView.findViewById(R.id.usn2);
                radioGroupAttendance = itemView.findViewById(R.id.radioGroupAttendance);
            }
        }
    }

    private void updateAttendanceStatus(String documentId, String username, String attendanceStatus) {
        Map<String, Object> data = new HashMap<>();
        data.put("Usn", documentId);
        data.put("Satatus", attendanceStatus);
        data.put("name", username);
        data.put(date.getText().toString(),attendanceStatus);
        data.put("subject",subjectWanted);

        CollectionReference studentCollection = db.collection("Student");
        DocumentReference dateDocument = studentCollection.document("ATTENDACE");

        if (attendanceStatus == "Present") {
            CollectionReference subjectCollection = dateDocument.collection(date.getText().toString());
            DocumentReference usnDocument = subjectCollection.document();
            usnDocument.set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Display a toast message to indicate successful update
                            Toast.makeText(take_attendace.this, "Attendance updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Display a toast message to indicate failure
                            Toast.makeText(take_attendace.this, "Failed to update attendance", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            CollectionReference subjectCollection = dateDocument.collection(date.getText().toString());
            DocumentReference usnDocument = subjectCollection.document();
            usnDocument.set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Display a toast message to indicate successful update
                            Toast.makeText(take_attendace.this, "Attendance updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Display a toast message to indicate failure
                            Toast.makeText(take_attendace.this, "Failed to update attendance", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}


