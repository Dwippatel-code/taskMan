package com.multitechdevelopers.taskman.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.multitechdevelopers.taskman.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setup();
        onClickListeners();
    }

    private void init() {
        sharedPreferences = getSharedPreferences("prefManager", MODE_PRIVATE);
    }

    private void setup() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getString("google_login", "").equals("")) {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
                }
                finishAffinity();
            }
        }, 3000L);

    }

    private void onClickListeners() {

    }

}