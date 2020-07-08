package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    TextView tvCode, tvName,tvCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvCode = findViewById(R.id.tvCode);
        tvName = findViewById(R.id.tvName);
        tvCompany = findViewById(R.id.tvCompany);
    }
}