package com.example.ifind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordUpdated extends AppCompatActivity {
    Button logInBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_updated);

        logInBttn = findViewById(R.id.logInBttn);
        logInBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PasswordUpdated.this, "Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PasswordUpdated.this, LoginActivity.class));
            }
        });
    }
}