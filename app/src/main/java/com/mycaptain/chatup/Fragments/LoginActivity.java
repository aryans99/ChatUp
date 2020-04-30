package com.mycaptain.chatup.Fragments;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.mycaptain.chatup.R;

public class LoginActivity extends AppCompatActivity {
    AppCompatEditText email,password;
    Button button_login;
    FirebaseAuth auth;
    TextView forgot_password;
    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.login);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        button_login=findViewById(R.id.button_login);
        forgot_password=findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });
        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String mail=email.getText().toString();
                String pass=password.getText().toString();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)){
                    Toast.makeText(Register.this,"All fields are required.", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(OnCompleteListener(new SoundPool.OnLoadCompleteListener<AuthResult>(){

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });



                }
            }
        });

    }
}
