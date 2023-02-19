package com.unipi.p19129_p19140.smartalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PreviousReportAdapter  extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    Context context;

    ArrayList<ReportModel> report_list;

    public PreviousReportAdapter(Context context, ArrayList<ReportModel> report_list) {
        this.context = context;
        this.report_list = report_list;
    }

    @NonNull
    @Override
    public ReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_report, parent, false);
        return new ReportAdapter.ReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ReportViewHolder holder, int position) {
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
}


