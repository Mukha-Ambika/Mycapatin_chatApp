package com.example.mycapatin_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class Register extends AppCompatActivity {
   MaterialEditText username,email,password;
   Button btn_register;
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
        username=findViewById(R.id.Username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.Password);
        btn_register=findViewById(R.id.REGISTER_regact);
        auth=FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username=username.getText().toString();
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                if(TextUtils.isEmpty(txt_username)||TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password))
                {
                    Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length()<6)
                {
                    Toast.makeText(Register.this, "password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
                }else
                {
                    register(txt_username,txt_email,txt_password);
                }

            }
        });
    }
    public void register(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isComplete())
                             {
                                 FirebaseUser firebaseuser=auth.getCurrentUser();
                                 String userid=firebaseuser.getUid();
                                 reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                 HashMap<String,String> hashMap=new HashMap<>();
                                 hashMap.put("id",userid);
                                 hashMap.put("username",username);
                                 hashMap.put("imageurl","default");
                                 hashMap.put("status","offline");
                                 hashMap.put("search",username.toLowerCase());
                                 reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful())
                                         {
                                             Intent intent=new Intent(Register.this,MainActivity.class);
                                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                             startActivity(intent);
                                             finish();
                                         }
                                     }
                                 });
                             }
                             else {
                                 Toast.makeText(Register.this, "You cant have this email or password for registration", Toast.LENGTH_LONG).show();

                             }
                            }
                        }
                );




    }
}