package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText client_code, company, user, password;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client_code = findViewById(R.id.client_code);
        company = findViewById(R.id.company);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);




    }
}
