package com.mmuhamadamirzaidi.sellynapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    LinearLayout setting_about_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        setting_about_us = (LinearLayout) findViewById(R.id.setting_about_us);

        setting_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "About us", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
