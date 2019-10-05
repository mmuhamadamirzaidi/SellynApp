package com.mmuhamadamirzaidi.sellynapp.Modules.Account;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Modules.Cart.CartActivity;
import com.mmuhamadamirzaidi.sellynapp.Modules.General.MainActivity;
import com.mmuhamadamirzaidi.sellynapp.Modules.General.SignInActivity;
import com.mmuhamadamirzaidi.sellynapp.R;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class SettingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout setting_about_us;

    ImageView header_profile_image;

    TextView header_fullname, header_identity_card;

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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        // Set user informations
        View headerView = navigationView.getHeaderView(0);
        header_fullname = (TextView) headerView.findViewById(R.id.header_fullname);
        header_identity_card = (TextView) headerView.findViewById(R.id.header_identity_card);
        header_profile_image = (ImageView) headerView.findViewById(R.id.header_profile_image);

        header_fullname.setText(Common.currentUser.getUserName());
        header_identity_card.setText(Common.currentUser.getUserIdentityCard());

        // Set image
        Picasso.with(getBaseContext()).load(Common.currentUser.getUserImage()).into(header_profile_image);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if (item.getItemId() == R.id.refresh){
//            loadCategory();
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Intent menuIntent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(menuIntent);

        }
        else if (id == R.id.nav_cart) {

            Intent menuIntent = new Intent(SettingActivity.this, CartActivity.class);
            startActivity(menuIntent);

        } else if (id == R.id.nav_order_status) {

            Intent menuIntent = new Intent(SettingActivity.this, OrderStatusActivity.class);
            startActivity(menuIntent);

        }else if (id == R.id.nav_wishlist) {

            Toast.makeText(SettingActivity.this, "Wishlist", Toast.LENGTH_SHORT).show();

        }
        else if (id == R.id.news) {

            Toast.makeText(SettingActivity.this, "News", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_account) {

            Intent accountIntent = new Intent(SettingActivity.this, AccountActivity.class);
            startActivity(accountIntent);

        }else if (id == R.id.nav_settings) {

            Intent settingIntent = new Intent(SettingActivity.this, SettingActivity.class);
            startActivity(settingIntent);

        }else if (id == R.id.nav_sign_out) {

            //Forget user information
            Paper.book().destroy();

            Intent menuIntent = new Intent(SettingActivity.this, SignInActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(menuIntent);
            Toast.makeText(SettingActivity.this, "Sign out successfully", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
