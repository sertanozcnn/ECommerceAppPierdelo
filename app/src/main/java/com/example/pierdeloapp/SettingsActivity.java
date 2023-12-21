package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);
        activityHeader.setText(getString(R.string.settings_title));
        backActivity.setOnClickListener(v -> finish());
        LinearLayout languageGoToActivity = findViewById(R.id.language);
        languageGoToActivity.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this,LanguageActivity.class);
            startActivity(intent);
        });

    }
}