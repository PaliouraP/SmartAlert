package com.unipi.p19129_p19140.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
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
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    GridView options;
    private ImageView menu_report,menu_logout,menu_home,menu_statistics;
    TextView greek_lan_btn, english_lan_btn;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menu_report=findViewById(R.id.menu_report);
        menu_logout=findViewById(R.id.logout_btn);
        menu_home=findViewById(R.id.home);
        menu_statistics=findViewById(R.id.menu_statistics);

        greek_lan_btn=findViewById(R.id.greek_language);
        english_lan_btn=findViewById(R.id.english_language);
        Menu_Buttons();
        gridview();
        Select_Greek();
        Select_English();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            uid =(String) b.get("uid");
        }
    }

    private void Menu_Buttons() {
        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });
        menu_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,PreviousReportsListActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });
        menu_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ReportActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
            }
        });
        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });

    }

    private void Select_English() {
        english_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
            }
        });
    }


    private void Select_Greek() {
        greek_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("el");
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));

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





    private void gridview(){
        options=findViewById(R.id.gridview);
        ArrayList<CardModel> cardModelArrayList= new ArrayList<CardModel>();
        String card1=getString(R.string.report_message);
        String card2=getString(R.string.statistics);
        cardModelArrayList.add(new CardModel(card1,R.drawable.siren));
        cardModelArrayList.add(new CardModel(card2,R.drawable.increase));

        CardAdapter adapter= new CardAdapter(this,cardModelArrayList);
        options.setAdapter(adapter);
        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent(HomeActivity.this,ReportActivity.class);
                        intent.putExtra("uid", uid);
                        break;
                    case 1:
                        intent = new Intent(HomeActivity.this,PreviousReportsListActivity.class);

                }
                startActivity(intent);
            }
        });
    }
}