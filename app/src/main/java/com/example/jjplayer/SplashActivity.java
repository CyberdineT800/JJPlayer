package com.example.jjplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    private static final long PHOTO_DISPLAY_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler to delay transitioning to main activity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainWindow.class));
            finish(); // Finish this activity to prevent going back to it when pressing back button
        }, PHOTO_DISPLAY_DURATION);
    }
}