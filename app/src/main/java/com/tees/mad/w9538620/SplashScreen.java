package com.tees.mad.w9538620;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE

                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedpreferences = getSharedPreferences("com.tees.mad.w9538620", Context.MODE_PRIVATE);
                Intent intent;
                boolean isLogedIn = sharedpreferences.getBoolean("isLoggedIn", false);
                if (isLogedIn) {
                    intent = new Intent(SplashScreen.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashScreen.this, OptionsActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}