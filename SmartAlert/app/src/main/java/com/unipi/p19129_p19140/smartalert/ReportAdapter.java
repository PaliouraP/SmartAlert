package com.unipi.p19129_p19140.smartalert;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>{

    Context context;

    ArrayList<ReportModel> report_list;

    public ReportAdapter(Context context, ArrayList<ReportModel> report_list) {
        this.context = context;
        this.report_list = report_list;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_report, parent, false);
        return new ReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {

        ReportModel report = report_list.get(position);
        holder.type.setText(report.getType());
        holder.location.setText(report.getLocation());
        holder.time.setText(report.getTimestamp());

        holder.reporter_sum.setText(String.valueOf(report.reporter_sum));

        holder.report_ids = report.reports;
    }

    @Override
    public int getItemCount() {
        return report_list.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView type, location, time, reporter_sum;
        Button accept_btn, decline_btn;
        //ReportModel report;
        ArrayList<String> report_ids;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.single_report_type);
            location = itemView.findViewById(R.id.single_report_location);
            time = itemView.findViewById(R.id.single_report_time);
            reporter_sum = itemView.findViewById(R.id.single_report_reporters_sum);
            accept_btn = itemView.findViewById(R.id.report_accept);
            decline_btn = itemView.findViewById(R.id.report_decline);


            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i=0; i<=report_ids.size()-1; i++){
                        FirebaseDatabase.getInstance().getReference().child("alerts").child(report_ids.get(i)).child("status").setValue("accepted");
                    }
                    //message and refresh
                }
            });
            decline_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i=0; i<=report_ids.size()-1; i++){
                        FirebaseDatabase.getInstance().getReference().child("alerts").child(report_ids.get(i)).child("status").setValue("declined");
                    }
                    //message and refresh
                }
            });
        }
    }
}
