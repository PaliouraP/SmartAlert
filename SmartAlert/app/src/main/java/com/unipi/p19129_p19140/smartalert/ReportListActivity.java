package com.unipi.p19129_p19140.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class ReportListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference db;
    ReportAdapter reportAdapter;
    HashMap<String, ReportModel> all_reports;
    HashMap<String, ReportModel> all_reports_filtered;
    ArrayList<ReportModel> report_list;
    TextView greek_lan_btn, english_lan_btn;
    private ImageView menu_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        greek_lan_btn=findViewById(R.id.greek_language);
        english_lan_btn=findViewById(R.id.english_language);
        menu_logout=findViewById(R.id.logout_btn);

        recyclerView = findViewById(R.id.report_list);
        db = FirebaseDatabase.getInstance().getReference("alerts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        all_reports = new HashMap<>();
        all_reports_filtered = new HashMap<>();
        report_list = new ArrayList<>();
        reportAdapter = new ReportAdapter(this, report_list);
        recyclerView.setAdapter(reportAdapter);

        //Language Functions
        Select_Greek();
        Select_English();
        //Menu Functions
        to_logout();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DistanceBetween disObj = new DistanceBetween();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ReportModel report = dataSnapshot.getValue(ReportModel.class);

                    all_reports.put(dataSnapshot.getKey(), report);


                }
                String s_type = "";
                int count = 0;
                Iterator<Map.Entry<String, ReportModel>> iterator = all_reports.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, ReportModel> entry = iterator.next();
                    String report_key = entry.getKey();
                    ReportModel report_value = entry.getValue();
                    if (report_value.getStatus().equals("pending") ) {

                        all_reports_filtered.put(report_key, report_value);
                    }
                }

                Log.d("valid reports", String.valueOf(all_reports_filtered.size()));

                iterator = all_reports_filtered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, ReportModel> entry = iterator.next();
                    String report_key = entry.getKey();
                    ReportModel report_value = entry.getValue();
                    s_type = report_value.getType();
                    count = 1;
                    ReportModel general_report = new ReportModel();
                    general_report.Type = s_type;
                    general_report.Location = report_value.getLocation();
                    general_report.Timestamp = report_value.getTimestamp();
                    ArrayList<String> report_ids = new ArrayList<>();
                    report_ids.add(report_key);


                    Iterator<Map.Entry<String, ReportModel>> other_iterator = all_reports.entrySet().iterator();
                    while (other_iterator.hasNext()) {
                        Map.Entry<String, ReportModel> other_entry = other_iterator.next();
                        String other_report_key = other_entry.getKey();
                        ReportModel other_report_value = other_entry.getValue();
                        boolean check_dis = disObj.checkDistance(general_report.Location, other_report_value.getLocation());
                        boolean time_dif = disObj.checkDuration(general_report.Timestamp, other_report_value.getTimestamp());
                        boolean dif_user = (!report_value.getUser().equals(other_report_value.getUser()));
                        if (other_report_value.getType().equals(s_type) && check_dis && time_dif && dif_user ) {
                            count++;
                            report_ids.add(other_report_key);
                        }
                    }
                    general_report.reporter_sum = count;
                    general_report.reports = report_ids;
                    if (count>1) {
                        report_list.add(general_report);
                    }
                }

                reportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void to_logout() {
        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportListActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });
    }

    private void Select_English() {
        english_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
                startActivity(new Intent(ReportListActivity.this,ReportListActivity.class));
            }
        });
    }

    private void Select_Greek() {
        greek_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("el");
                startActivity(new Intent(ReportListActivity.this,ReportListActivity.class));

            }
        });
    }

    private void setLanguage(String code) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(new Locale(code.toLowerCase()));
        }
        else {
            configuration.locale= new Locale(code.toLowerCase());

        }
        resources.updateConfiguration(configuration,displayMetrics);

    }
}