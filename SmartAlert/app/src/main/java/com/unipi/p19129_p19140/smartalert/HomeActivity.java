package com.unipi.p19129_p19140.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ImageView logout;
    GridView options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logout=findViewById(R.id.logout_btn);
        to_logout();
        gridview();



    }
    private void to_logout(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });
    }

    private void gridview(){
        options=findViewById(R.id.gridview);
        ArrayList<CardModel> cardModelArrayList= new ArrayList<CardModel>();
        cardModelArrayList.add(new CardModel("Report an incident",R.drawable.danger));
        cardModelArrayList.add(new CardModel("Report an incident",R.drawable.danger));

        CardAdapter adapter= new CardAdapter(this,cardModelArrayList);
        options.setAdapter(adapter);
        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView message=(TextView) view.findViewById(R.id.TexV);
                Toast.makeText(HomeActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent(HomeActivity.this,ReportActivity.class);
                        break;
                    case 1:
                        intent = new Intent(HomeActivity.this,LoginActivity.class);

                }
                startActivity(intent);
            }
        });
    }
}