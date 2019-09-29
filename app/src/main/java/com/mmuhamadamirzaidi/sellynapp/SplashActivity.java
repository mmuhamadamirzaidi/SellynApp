package com.mmuhamadamirzaidi.sellynapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Model.User;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    private ImageView SplashLogo;
    private int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashLogo = (ImageView)findViewById(R.id.splash_logo_startup);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent splash = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(splash);
                finish();
            }
        },SPLASH_TIME_OUT);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        SplashLogo.startAnimation(myanim);
    }
}
