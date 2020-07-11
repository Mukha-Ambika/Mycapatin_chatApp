package com.example.mycapatin_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Reset_password extends AppCompatActivity {
MaterialEditText send_email;
Button btn_reset;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send_email=findViewById(R.id.reset_email);
        btn_reset=findViewById(R.id.REGISTER_regact);
        firebaseAuth=FirebaseAuth.getInstance();
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=send_email.getText().toString();
                if(email.equals(""))
                {
                    Toast.makeText(Reset_password.this, "ALL feilds are required", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Reset_password.this, "please check your email to reset your passwordn", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Reset_password.this,Activity_login.class));
                            }else{
                                String error=task.getException().getMessage();
                                Toast.makeText(Reset_password.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}