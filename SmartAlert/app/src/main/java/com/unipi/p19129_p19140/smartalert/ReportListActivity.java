package com.unipi.p19129_p19140.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

public class ReportListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference db;
    ReportAdapter reportAdapter;
    ArrayList<ReportModel> all_reports;
    ArrayList<ReportModel> all_reports_filtered;
    ArrayList<ReportModel> report_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        recyclerView = findViewById(R.id.report_list);
        db = FirebaseDatabase.getInstance().getReference("alerts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        all_reports = new ArrayList<>();
        all_reports_filtered = new ArrayList<>();
        report_list = new ArrayList<>();
        reportAdapter = new ReportAdapter(this, report_list);
        recyclerView.setAdapter(reportAdapter);

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

                reportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}