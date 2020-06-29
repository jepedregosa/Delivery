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

public class SplashActivity extends AppCompatActivity {
    final String TAG = "SplashActivity";
    ProgressBar splashProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(BuildConfig.APPLICATION_ID + ".credentials", Context.MODE_PRIVATE);
        readSharedPreferences();

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();
    }

    SharedPreferences sharedpreferences;

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
            }, 2500);
        } else {
            Log.d(TAG, "Logging in...");
        }
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(2500)
                .start();
    }
}
