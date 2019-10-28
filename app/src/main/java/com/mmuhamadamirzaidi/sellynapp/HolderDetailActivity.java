package com.mmuhamadamirzaidi.sellynapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Model.Holder;
import com.squareup.picasso.Picasso;

public class HolderDetailActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference holder;

    ImageView holder_image;

    TextView detail_holder_name,
            detail_holder_phone, detail_holder_address_line_1,
            detail_holder_address_line_2,
            detail_holder_address_line_3, detail_holder_district, detail_holder_postcode, detail_holder_state;

    String holderId="";

    Holder currentHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder_detail);

        // Hide the Status Bar and the Navigation Bar

        View overlay = findViewById(R.id.activity_holder_detail);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.holder_detail_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        holder = database.getReference("Holder");

        // Init view
        holder_image = (ImageView) findViewById(R.id.holder_image);

        detail_holder_name = (TextView) findViewById(R.id.detail_holder_name);
        detail_holder_phone = (TextView) findViewById(R.id.detail_holder_phone);
        detail_holder_address_line_1 = (TextView) findViewById(R.id.detail_holder_address_line_1);
        detail_holder_address_line_2 = (TextView) findViewById(R.id.detail_holder_address_line_2);
        detail_holder_address_line_3 = (TextView) findViewById(R.id.detail_holder_address_line_3);
        detail_holder_district = (TextView) findViewById(R.id.detail_holder_district);
        detail_holder_postcode = (TextView) findViewById(R.id.detail_holder_postcode);
        detail_holder_state = (TextView) findViewById(R.id.detail_holder_state);

        // Get productId intent
        if (getIntent() != null){
            holderId = getIntent().getStringExtra("holderId");
        }
        if (!holderId.isEmpty() && holderId != null){

            if (Common.isConnectedToInternet(this)){
                loadHolderDetail(holderId);
            }
            else{
                Toast.makeText(HolderDetailActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void loadHolderDetail(String holderId) {
        holder.child(holderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentHolder = dataSnapshot.getValue(Holder.class);

                // Set image
                Picasso.with(getBaseContext()).load(currentHolder.getManufacturerImage()).into(holder_image);

                detail_holder_name.setText(currentHolder.getManufacturerName());
                detail_holder_phone.setText(currentHolder.getManufacturerPhoneNo());
                detail_holder_address_line_1.setText(currentHolder.getManufacturerAddress1());
                detail_holder_address_line_2.setText(currentHolder.getManufacturerAddress2());
                detail_holder_address_line_3.setText(currentHolder.getManufacturerAddress3());
                detail_holder_district.setText(currentHolder.getManufacturerDistrict());
                detail_holder_postcode.setText(currentHolder.getManufacturerPostcode());
                detail_holder_state.setText(currentHolder.getManufacturerState());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
