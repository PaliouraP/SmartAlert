package com.unipi.p19129_p19140.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private FirebaseAuth auth;
    private ImageView add;
    private TextView to_register,greek_lan_btn, english_lan_btn;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_btn);
        add = findViewById(R.id.add);
        to_register =findViewById(R.id.register_btn);
        greek_lan_btn=findViewById(R.id.greek_language);
        english_lan_btn=findViewById(R.id.english_language);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                loginUser(txt_email, txt_password);
            }


        });
        //Language Functions
        Select_Greek();
        Select_English();
        //Menu Functions
        to_register();
    }

    private void Select_English() {
        english_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
                startActivity(new Intent(LoginActivity.this,LoginActivity.class));
            }
        });
    }

    private void Select_Greek() {
        greek_lan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("el");
                startActivity(new Intent(LoginActivity.this,LoginActivity.class));

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

    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email.trim(), password.trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                ref = FirebaseDatabase.getInstance().getReference().child("roles").child(firebaseUser.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            String userRole = snapshot.getValue().toString();
                            if(userRole.equals("advanced")){
                                startActivity(new Intent(LoginActivity.this, ReportListActivity.class));
                                Toast.makeText(LoginActivity.this, "Successful login!", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent ii=new Intent(LoginActivity.this, HomeActivity.class);
                                ii.putExtra("uid", firebaseUser.getUid());
                                startActivity(ii);
                                Toast.makeText(LoginActivity.this, "Successful login!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Throwable e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                finish();
            }
        });
    }
    private void to_register(){
        to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });

    }

}