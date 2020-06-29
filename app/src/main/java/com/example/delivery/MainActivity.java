package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    EditText client_code, company, user, password;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(BuildConfig.APPLICATION_ID + ".credentials", Context.MODE_PRIVATE);

        client_code = findViewById(R.id.client_code);
        company = findViewById(R.id.company);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
    }

    SharedPreferences sharedpreferences;

    private void login() {
        String client_code_text = client_code.getText().toString();
        String company_text = company.getText().toString();
        String user_text = user.getText().toString();
        String password_text = password.getText().toString();

        if (client_code_text.matches("") && company_text.matches("")
            && user_text.matches("") && password_text.matches("")) {
            Toast.makeText(getApplicationContext(), "All credentials must be filled.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("client_code", client_code_text);
            editor.putString("company", company_text);
            editor.putString("user", user_text);
            editor.putString("password", password_text);
            editor.commit();
        }

    }
}
