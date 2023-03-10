package com.unipi.p19129_p19140.smartalert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

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
        holder.type_name = report.getType();
        holder.location_name = report.getLocation();
        holder.time_stamp = report.getTimestamp();
        holder.reporters = String.valueOf(report.reporter_sum);


    }

    @Override
    public int getItemCount() {
        return report_list.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView type, location, time, reporter_sum;
        Button accept_btn, decline_btn;
        ArrayList<String> report_ids;
        String type_name, location_name, reporters, time_stamp;

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
                    Intent intent = new Intent(view.getContext(),ConfirmAcceptActivity.class);
                    intent.putExtra("type", type_name);
                    intent.putExtra("time", time_stamp);
                    intent.putExtra("location", location_name);
                    intent.putExtra("report_num", reporters);
                    intent.putExtra("reporter_ids", report_ids);
                    view.getContext().startActivity(intent);

                }
            });
            decline_btn.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    for (int i=0; i<=report_ids.size()-1; i++){
                        FirebaseDatabase.getInstance().getReference().child("alerts").child(report_ids.get(i)).child("status").setValue("declined");
                    }
                    Toast.makeText(view.getContext(),"Report Declined",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(),ReportActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }

    }
}
