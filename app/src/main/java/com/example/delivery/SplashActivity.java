package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    final String TAG = "SplashActivity";
    final int DURATION = 2500;
    ProgressBar splashProgress;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(BuildConfig.APPLICATION_ID + ".credentials", Context.MODE_PRIVATE);
        readSharedPreferences();

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();
    }

    private void readSharedPreferences() {
        Log.d(TAG, "reading shared preferences");
        Log.d(TAG, sharedpreferences.getString("client_code", "null"));
        Log.d(TAG, sharedpreferences.getString("company", "null"));
        Log.d(TAG, sharedpreferences.getString("user", "null"));
        Log.d(TAG, sharedpreferences.getString("password", "null"));

        tryLogin(sharedpreferences.getString("client_code", ""),
            sharedpreferences.getString("company", ""),
            sharedpreferences.getString("user", ""),
            sharedpreferences.getString("password", "")
        );
    }

    private void tryLogin(String client_code, String company, String user, String password) {
        Log.d(TAG, "Trying to log in...");

        if (client_code.matches("") || company.matches("")
            || user.matches("") || password.matches("")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mySuperIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                    Log.d(TAG, "Moving to new activity");
                }
            }, DURATION);
        } else {
            Log.d(TAG, "Logging in...");
            login(client_code, company, user, password);
        }
    }

    private void login(final String client_code, final String company, final String user, final String password) {
        String URL = "http://192.168.254.100:80/api/users";

        if (client_code.matches("") && company.matches("")
                && user.matches("") && password.matches("")) {
            Toast.makeText(getApplicationContext(), "All credentials must be filled.", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("client_code", client_code);
                jsonObject.put("company", company);
                jsonObject.put("username", user);
                jsonObject.put("password", password);
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
                            editor.putString("client_code", client_code);
                            editor.putString("company", company);
                            editor.putString("user", user);
                            editor.putString("password", password);
                            editor.commit();

                            Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();

                            Intent mySuperIntent = new Intent(SplashActivity.this, Main_Screen.class);
                            startActivity(mySuperIntent);
                            finish();
                            Log.d(TAG, "Moving to new activity");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response", error.toString());
                            Intent mySuperIntent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(mySuperIntent);
                            finish();
                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
        }

    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(DURATION)
                .start();
    }
}
