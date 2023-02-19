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
import java.util.Locale;

public class PreviousReportsListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference db;
    PreviousReportAdapter previousreportAdapter;
    ArrayList<ReportModel> all_reports;
    ArrayList<ReportModel> all_reports_filtered;
    ArrayList<ReportModel> report_list;
    TextView greek_lan_btn, english_lan_btn;
    private ImageView menu_logout,image_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_reports_list);

        greek_lan_btn=findViewById(R.id.greek_language);
        english_lan_btn=findViewById(R.id.english_language);
        menu_logout=findViewById(R.id.logout_btn);

        recyclerView = findViewById(R.id.report_list);
        db = FirebaseDatabase.getInstance().getReference("alerts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        all_reports = new ArrayList<>();
        all_reports_filtered = new ArrayList<>();
        report_list = new ArrayList<>();
        previousreportAdapter = new PreviousReportAdapter(this,report_list);
        recyclerView.setAdapter(previousreportAdapter);

        //Language Functions
        Select_Greek();
        Select_English();
        //Menu Functions
        to_logout();
        //Filling List
        Filling_List();
    }

    private void Filling_List() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DistanceBetween disObj = new DistanceBetween();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ReportModel report = dataSnapshot.getValue(ReportModel.class);

                    all_reports.add(report);

                }
                String s_type = "";
                int count = 0;
                for (int temp = 0; temp < all_reports.size(); temp++) {
                    // && disObj.beforeNow(all_reports.get(temp).getTimestamp())
                    if (all_reports.get(temp).getStatus().equals("pending") ) {
                        all_reports_filtered.add(all_reports.get(temp));
                    }
                }
                Log.d("valid reports", String.valueOf(all_reports_filtered.size()));
                for (int i = 0; i < all_reports_filtered.size(); i++){
                    s_type = all_reports_filtered.get(i).getType();
                    count = 1;
                    ReportModel general_report = new ReportModel();
                    general_report.Type = s_type;
                    general_report.Location = all_reports_filtered.get(i).getLocation();
                    general_report.Timestamp = all_reports_filtered.get(i).getTimestamp();

                    ChangeImage(s_type);

                    for(int j = i+1; j < all_reports_filtered.size(); j++){
                        boolean check_dis = disObj.checkDistance(all_reports_filtered.get(i).getLocation(), all_reports_filtered.get(j).getLocation());
                        boolean time_dif = disObj.checkDuration(all_reports_filtered.get(i).getTimestamp(), all_reports_filtered.get(j).getTimestamp());
                        boolean dif_user = (!all_reports_filtered.get(i).getUser().equals(all_reports_filtered.get(j).getUser()));
                        if (all_reports_filtered.get(j).getType().equals(s_type) && check_dis && time_dif && dif_user ) {
                            count++;
                        }
                    }
                    general_report.reporter_sum = count;
                    if (count>1) {
                        report_list.add(general_report);
                    }
                    Log.d("report", String.valueOf(i));
                }

                previousreportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ChangeImage(String type) {
        switch (type){
            case "Fire":


        }
        if (type=="Fire"){

        }
    }

    private void to_logout() {
        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PreviousReportsListActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });
    }

    private void Select_English() {
        english_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
                startActivity(new Intent(PreviousReportsListActivity.this,PreviousReportsListActivity.class));
            }
        });
    }

    private void Select_Greek() {
        greek_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("el");
                startActivity(new Intent(PreviousReportsListActivity.this,PreviousReportsListActivity.class));

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