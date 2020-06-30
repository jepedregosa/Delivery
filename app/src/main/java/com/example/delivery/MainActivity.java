package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    EditText client_code, company, user, password;
    Button login_btn;
    SharedPreferences sharedpreferences;

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

    private void login() {
        String URL = "http://" + getString(R.string.host_port) + "/api/users";
        final String client_code_text = client_code.getText().toString();
        final String company_text = company.getText().toString();
        final String user_text = user.getText().toString();
        final String password_text = password.getText().toString();

        if (client_code_text.matches("") && company_text.matches("")
            && user_text.matches("") && password_text.matches("")) {
            Toast.makeText(getApplicationContext(), "All credentials must be filled.", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("client_code", client_code_text);
                jsonObject.put("company", company_text);
                jsonObject.put("username", user_text);
                jsonObject.put("password", password_text);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    URL,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Rest Response", response.toString());
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("client_code", client_code_text);
                            editor.putString("company", company_text);
                            editor.putString("user", user_text);
                            editor.putString("password", password_text);
                            editor.commit();

                            Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_SHORT).show();

                            Intent mySuperIntent = new Intent(MainActivity.this, Main_Screen.class);
                            startActivity(mySuperIntent);
                            finish();
                            Log.d(TAG, "Moving to new activity");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response", error.toString());
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
        }

    }
}
