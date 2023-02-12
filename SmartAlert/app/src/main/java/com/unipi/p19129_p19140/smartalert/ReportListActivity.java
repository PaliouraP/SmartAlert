package com.unipi.p19129_p19140.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.Objects;

public class ReportListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference db;
    ReportAdapter reportAdapter;
    ArrayList<ReportModel> all_reports;
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
        report_list = new ArrayList<>();
        reportAdapter = new ReportAdapter(this, report_list);
        recyclerView.setAdapter(reportAdapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ReportModel report = dataSnapshot.getValue(ReportModel.class);
                    all_reports.add(report);
                }
                String s_type = "";
                int count = 0;
                for (int i = 0; i < all_reports.size(); i++){
                    s_type = all_reports.get(i).getType();
                    count = 1;
                    ReportModel general_report = new ReportModel();
                    general_report.Type = s_type;
                    general_report.Location = all_reports.get(i).getLocation();
                    general_report.Timestamp = all_reports.get(i).getTimestamp();
                    for(int j = i+1; j < all_reports.size(); j++){
                        if (all_reports.get(j).getType().equals(s_type)) {
                            count++;

                        }
                    }
                    general_report.reporter_sum = count;
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
}