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

public class ReportListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference db;
    ReportAdapter reportAdapter;
    ArrayList<ReportModel> report_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        recyclerView = findViewById(R.id.report_list);
        db = FirebaseDatabase.getInstance().getReference("alerts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        report_list = new ArrayList<>();
        reportAdapter = new ReportAdapter(this, report_list);
        recyclerView.setAdapter(reportAdapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ReportModel report = dataSnapshot.getValue(ReportModel.class);
                    report_list.add(report);
                }
                reportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}