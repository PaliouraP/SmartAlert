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
import java.util.List;

public class PreviousReportAdapter extends RecyclerView.Adapter<PreviousReportAdapter.PreviousReportViewHolder>{

    Context context;

    ArrayList<ReportModel> report_list;

    public PreviousReportAdapter(Context context, ArrayList<ReportModel> report_list) {
        this.context = context;
        this.report_list = report_list;
    }

    @NonNull
    @Override
    public PreviousReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_previous_report, parent, false);
        return new PreviousReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousReportViewHolder holder, int position) {
        ReportModel report = report_list.get(position);
        holder.type.setText(report.getType());

        holder.location.setText(report.getLocation());
        holder.time.setText(report.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return report_list.size();
    }

    public static class PreviousReportViewHolder extends RecyclerView.ViewHolder{
        TextView type, location, time;
        public PreviousReportViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.single_previous_report_type);
            location = itemView.findViewById(R.id.single_previous_report_location);
            time = itemView.findViewById(R.id.single_previous_report_time);
        }
    }
}


