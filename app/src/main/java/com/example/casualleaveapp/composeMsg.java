package com.example.casualleaveapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class composeMsg extends AppCompatActivity {
    private ArrayList<String> alldates =new ArrayList<String>();
    private GalleryAdapter galleryAdapter;
    private GridView gvGallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_msg);
        Button addD = findViewById(R.id.addDate);
        gvGallery = findViewById(R.id.dateset);
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
    }
}
