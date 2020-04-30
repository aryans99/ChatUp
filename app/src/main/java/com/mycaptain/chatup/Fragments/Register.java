package com.mycaptain.chatup.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mycaptain.chatup.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    MaterialEditText username,email,password;
    Button button_register;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        button_register=findViewById(R.id.register);
        auth=FirebaseAuth.getInstance();

        button_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String user=username.getText().toString();
                String mail=email.getText().toString();
                String pass=password.getText().toString();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)){
                    Toast.makeText(Register.this,"All fields are required.", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<6){
                    Toast.makeText(Register.this, "Password must be atleast 6 characters long.", Toast.LENGTH_SHORT).show();
                }
                else{
                    register(user,mail,pass);
                }
            }
        });
    }
    //add users to database
    public void register(String user,String mail, String pass){
        auth.createUserWithEmailAndPassword(email,password);
            .addOnCompleteListener(
                new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userid =firebaseUser.getUid();
                            reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("status","offline");
                            hashMap.put("search",username.toLowerCase());
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>(){


                                @Override
                                public void onComplete(@NonNull Task<Void> task){
                               if(task.isSuccessful()){
                                   Intent intent= new Intent(Register.this,MainActivity.class);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(intent);
                                   finish();

                                }
                            });


                        });
                        }
                        else {
                            Toast.makeText(Register.this,"You can't have this email or password for registration",Toast.LENGTH_LONG).show();

                            }

                    }
                }
        );
    }

    }
