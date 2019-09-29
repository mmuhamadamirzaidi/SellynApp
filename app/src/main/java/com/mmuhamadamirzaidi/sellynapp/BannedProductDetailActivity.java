package com.mmuhamadamirzaidi.sellynapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Model.BannedProduct;
import com.mmuhamadamirzaidi.sellynapp.Model.Product;
import com.squareup.picasso.Picasso;

public class BannedProductDetailActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference bannedproduct;

    ImageView banned_product_detail_image;

    TextView banned_product_detail_product_name, banned_product_detail_notification_no, banned_product_detail_holder_name, banned_product_detail_manufacturer_name, banned_product_detail_product_description, banned_product_detail_prohibited_ingredient;

    String bannedProductId="";

    BannedProduct currentbannedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned_product_detail);

        Toolbar toolbar = findViewById(R.id.banned_product_detail_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        bannedproduct = database.getReference("BannedProduct");

        // Init view
        banned_product_detail_image = (ImageView) findViewById(R.id.banned_product_detail_image);

        banned_product_detail_product_name = (TextView) findViewById(R.id.banned_product_detail_product_name);
        banned_product_detail_notification_no = (TextView) findViewById(R.id.banned_product_detail_notification_no);
        banned_product_detail_holder_name = (TextView) findViewById(R.id.banned_product_detail_holder_name);
        banned_product_detail_manufacturer_name = (TextView) findViewById(R.id.banned_product_detail_manufacturer_name);
        banned_product_detail_product_description = (TextView) findViewById(R.id.banned_product_detail_product_description);
        banned_product_detail_prohibited_ingredient = (TextView) findViewById(R.id.banned_product_detail_prohibited_ingredient);

        // Get productId intent
        if (getIntent() != null){
            bannedProductId = getIntent().getStringExtra("bannedProductId");
        }
        if (!bannedProductId.isEmpty() && bannedProductId != null){

            if (Common.isConnectedToInternet(this)){
                loadBannedProductDetail(bannedProductId);
            }
            else{
                Toast.makeText(BannedProductDetailActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void loadBannedProductDetail(String bannedProductId) {
        bannedproduct.child(bannedProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentbannedProduct = dataSnapshot.getValue(BannedProduct.class);

                // Set image
                Picasso.with(getBaseContext()).load(currentbannedProduct.getProductImage()).into(banned_product_detail_image);

                banned_product_detail_product_name.setText(currentbannedProduct.getProductName());
                banned_product_detail_notification_no.setText(currentbannedProduct.getNotificationNo());
                banned_product_detail_holder_name.setText(currentbannedProduct.getProductHolder());
                banned_product_detail_manufacturer_name.setText(currentbannedProduct.getProductManufacturer());
                banned_product_detail_product_description.setText(currentbannedProduct.getProductDescription());
                banned_product_detail_prohibited_ingredient.setText(currentbannedProduct.getProhibitedIngredient());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
