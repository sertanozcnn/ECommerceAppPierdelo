package com.example.pierdeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    Button rbTurkish,rbEnglish;
    private boolean isRecreating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);
        activityHeader.setText(getString(R.string.xml_language));
        backActivity.setOnClickListener(v -> finish());


        rbTurkish = findViewById(R.id.rb_turkish);
        rbEnglish = findViewById(R.id.rb_english);



    }


}