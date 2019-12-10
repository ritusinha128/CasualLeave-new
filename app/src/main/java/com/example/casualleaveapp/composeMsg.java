package com.example.casualleaveapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class composeMsg extends AppCompatActivity {
    private ArrayList<String> alldates =new ArrayList<String>();
    private GalleryAdapter galleryAdapter;
    private GridView gvGallery;
    private EditText et;
    private DatabaseReference mDatabase;
    Button btn;
    FirebaseFirestore db;
    String TAG = "RITUU";
    FirebaseUser mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_msg);
        Button addD = findViewById(R.id.addDate);
        gvGallery = findViewById(R.id.dateset);
        btn = findViewById(R.id.button2);
        db = FirebaseFirestore.getInstance();

        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        et = findViewById(R.id.multiAutoCompleteTextView);
        addD.setOnClickListener(new View.OnClickListener() {

            String date;
            DatePickerDialog datePickerDialog;
            int year;
            int month;
            int dayOfMonth;
            Calendar calendar;
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(composeMsg.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date = day + "/" + (month + 1) + "/" + year;
                                alldates.add(date);
                                galleryAdapter = new GalleryAdapter(getApplicationContext(),alldates);
                                gvGallery.setAdapter(galleryAdapter);
                                gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                        .getLayoutParams();
                                mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> city = new HashMap<>();
                mAuth = FirebaseAuth.getInstance().getCurrentUser();
                String uid = mAuth.getUid();
                city.put("Teacher","Ritu");
                city.put("isApproved", "Not");
                city.put("Reason", et.getText().toString());
                city.put("Dates", alldates);
                city.put ("Substitutes", new ArrayList<String>());


                db.collection("Department").document(uid)
                        .set(city, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                Toast.makeText(getApplicationContext(), "Database updated", Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

    }
}
