package com.unipi.p19129_p19140.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ReportActivity extends AppCompatActivity implements LocationListener {

    private Spinner type;
    private EditText details;
    private ImageView report;
    private Button upload;
    private ImageView menu_report,menu_logout;
    TextView greek_lan_btn, english_lan_btn;

    // Location manager object
    LocationManager locationManager;
    double latitude;
    double longitude;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    //Firebase Storage
    private  FirebaseStorage storage;
    private StorageReference storageReference;
    Uri selectedImageUri;
    boolean uploaded;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        type = findViewById(R.id.report_type);
        details = findViewById(R.id.report_details);
        report = findViewById(R.id.report_btn);
        upload = findViewById(R.id.upload_img);
        menu_report=findViewById(R.id.menu_report);
        menu_logout=findViewById(R.id.logout_btn);
        greek_lan_btn=findViewById(R.id.greek_language);
        english_lan_btn=findViewById(R.id.english_language);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            uid =(String) b.get("uid");
        }

        // Setting spinner items
        String[] items = new String[]{"Fire", "Flood", "Dangerous Weather", "Earthquake","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        type.setAdapter(adapter);
        //CALLING GET_LOCATION FUNCTION
        Get_Location();
        //Language Change
        Select_Greek();
        Select_English();

        // UPLOAD IMAGE BUTTON
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }

            private void chooseImage() {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
        // CLICKING ON THE REPORT BUTTON ON THE MENU
        menu_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportActivity.this,ReportActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });
        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });

        // ON CLICK LISTENER FOR REPORT BUTTON
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_type = type.getSelectedItem().toString();
                String txt_details = details.getText().toString();

                if (TextUtils.isEmpty(txt_details) || TextUtils.isEmpty((txt_type))) {
                    Toast.makeText(ReportActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                } else {
                    makeReport(txt_type, txt_details);
                }
            }
        });

    }

    private void Select_English() {
        english_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
                startActivity(new Intent(ReportActivity.this,ReportActivity.class));

            }
        });
    }

    private void Select_Greek() {
        greek_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("el");
                startActivity(new Intent(ReportActivity.this,ReportActivity.class));
            }
        });
    }

    private void setLanguage(String code) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(code.toLowerCase()));
        resources.updateConfiguration(configuration,displayMetrics);
    }

    // CHECK IF IMAGE WAS UPLOADED AND SAVE CHOSEN IMAGE
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 &&  resultCode == RESULT_OK && data!=null && data.getData()!=null) {
                selectedImageUri = data.getData();
                uploaded=true;
        }
    }

    // GET LOCATION
    public void Get_Location() {
        // IF PERMISSION ACCEPTED
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
    }

    // HANDLING PERMISSION RESULT
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();

    }

    // REPORT FUNCTION IS CALLED WHEN PRESSING THE ADD REPORT BUTTON
    private void makeReport(String type, String details) {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        Date date = new Date();
        String timestamp = String.valueOf(formatter.format(date));
        String user_location=latitude+","+longitude;
        uploadImage(type,user_location);
        HashMap<String, Object> map = new HashMap<>();
        map.put("User", uid);
        map.put("Location", user_location); 
        map.put("Timestamp", timestamp);
        map.put("Type", type);
        map.put("Details", details);
        map.put("status", "pending");
        FirebaseDatabase.getInstance().getReference().child("alerts").push().updateChildren(map);
        Toast.makeText(ReportActivity.this, "Reported successfully!", Toast.LENGTH_SHORT).show();
    }

    // UPLOADING IMAGE IN FIREBASE STORAGE FOLDER "images"
    public void uploadImage(String type,String location_saved) {
        if(uploaded){
        final String key= UUID.randomUUID().toString();
        // EVERY IMAGE IS NAMED AFTER TYPE OF DISASTER AND ITS LOCATION
        StorageReference s=storageReference.child("images/"+type+"_"+location_saved);
        s.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Image was not uploaded",Toast.LENGTH_LONG).show();
                    }
                });
    }
    }


}
