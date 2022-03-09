package com.tees.mad.w9538620;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

public class OptionsActivity extends AppCompatActivity {

    AppCompatSpinner spinner;
    String[] roles = {"Listing Agent", "Owner"};
    private boolean verifyOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
/*
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.logo);*/

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        aa.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) verifyOwner = false;
                else if (position == 1) verifyOwner = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void register(View view) {
        startActivity(new Intent(this, UserRegistration.class).putExtra("verifyOwner", verifyOwner));

    }

    public void login(View view) {
        startActivity(new Intent(this, UserLogin.class).putExtra("verifyOwner", verifyOwner));
    }
}