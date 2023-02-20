package com.unipi.p19129_p19140.smartalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class ConfirmAcceptActivity extends AppCompatActivity {
    private Button confirm;
    String no_reports, time, place, type;
    ArrayList<String> report_ids;
    TextView type_lbl, time_lbl, location_lbl, report_num_lbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_accept);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            no_reports = (String) b.get("report_num");
            time = (String) b.get("time");
            place = (String) b.get("location");
            type = (String) b.get("type");
            report_ids = (ArrayList<String>) b.get("reporter_ids");

        }

        String messageId = UUID.randomUUID().toString(); // generating random id

        confirm = findViewById(R.id.report_confirm);
        type_lbl = findViewById(R.id.confirm_report_type);
        location_lbl = findViewById(R.id.confirm_report_location);
        time_lbl = findViewById(R.id.confirm_report_time);
        report_num_lbl = findViewById(R.id.confirm_report_reporters_sum);

        type_lbl.setText(type);
        location_lbl.setText(place);
        time_lbl.setText(time);
        report_num_lbl.setText(no_reports);

        NotificationChannel channel = new NotificationChannel(
                messageId,
                "alert",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0; i<=report_ids.size()-1; i++){
                    FirebaseDatabase.getInstance().getReference().child("alerts").child(report_ids.get(i)).child("status").setValue("accepted");
                }


                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        ConfirmAcceptActivity.this,
                        messageId
                );
                builder.setContentTitle("Emergency Alert");
                builder.setContentText("There is an alert of type '" + type + "' near you.");
                builder.setSmallIcon(R.drawable.ic_launcher_background);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ConfirmAcceptActivity.this);
                managerCompat.notify(1, builder.build());
                Toast.makeText(view.getContext(),"Report Accepted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(),ReportListActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }
}