package com.unipi.p19129_p19140.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private Spinner type;
    private EditText details;
    private Button report;

    private LocationManager locationManager;
    double latitude;
    double longitude;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        type = findViewById(R.id.report_type);
        details = findViewById(R.id.report_details);
        report = findViewById(R.id.report_btn);


        // Setting spinner items
        String[] items = new String[]{"Fire", "Flood", "Dangerous Weather", "Earthquake", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        type.setAdapter(adapter);



        // location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_type = type.getSelectedItem().toString();
                String txt_details = details.getText().toString();

                if (TextUtils.isEmpty(txt_details) || TextUtils.isEmpty((txt_type))){
                    Toast.makeText(ReportActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                } else {
                    makeReport(txt_type, txt_details);
                }
            }
        });
    }


    private void makeReport(String type, String details) {
        String timestamp = String.valueOf(Calendar.getInstance().getTime());
        HashMap<String, Object> map = new HashMap<>();
        map.put("User", "user id"); //change to actual user id
        map.put("Location", "0,0"); // change to actual location
        map.put("Timestamp", timestamp);
        map.put("Type", type);
        map.put("Details", details);
        map.put("Approved", false);
        FirebaseDatabase.getInstance().getReference().child("alerts").push().updateChildren(map);
        Toast.makeText(ReportActivity.this, "Reported successfully!", Toast.LENGTH_SHORT).show();
    }


}