package com.unipi.p19129_p19140.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password,password_confirm;
    private Button register;
    private FirebaseAuth auth;
    private TextView to_login;
    private ImageView add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password_confirm=findViewById(R.id.confirm_password);
        register = findViewById(R.id.register_btn);
        to_login = findViewById(R.id.login_btn);
        add=findViewById(R.id.add);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_password_confirm = password_confirm.getText().toString();


                if (TextUtils.isEmpty(txt_email.trim()) || TextUtils.isEmpty((txt_password.trim()))){
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.trim().length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password needs to be 6 characters or longer!", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_password.trim().equals(txt_password_confirm.trim())){
                    Toast.makeText(RegisterActivity.this, "Passwords did not match!", Toast.LENGTH_SHORT).show();

                }
                else {
                    registerUser(txt_email.trim(), txt_password.trim());
                }
            }
        });

        to_login();
    }

    private void to_login(){
        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
            }
        });


    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Successful registration!", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("roles").child(auth.getUid()).setValue("normal"); // default user registering is normal
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}