package com.mmuhamadamirzaidi.sellynapp.Modules.Account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.mmuhamadamirzaidi.sellynapp.R;

public class ProfileActivity extends AppCompatActivity {

    ImageView back, edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        back = findViewById(R.id.back);
        edit = findViewById(R.id.edit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(menuIntent);
            }
        });
    }
}
