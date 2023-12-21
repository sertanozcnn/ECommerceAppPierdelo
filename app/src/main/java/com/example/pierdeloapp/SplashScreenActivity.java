package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int LIMIT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> checkAuthenticationAndNavigate(),LIMIT);



    }
    private void checkAuthenticationAndNavigate() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(SplashScreenActivity.this, NavigationActivity.class));
        }
        finish();
    }

}