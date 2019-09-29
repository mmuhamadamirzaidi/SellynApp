package com.mmuhamadamirzaidi.sellynapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OrderDetailActivity extends AppCompatActivity {

    TextView order_detail_grand_total, order_detail_id, order_detail_status, order_detail_full_name, order_detail_address, order_detail_phone;
    TextView order_detail_sub_total, order_detail_delivery_charge, order_detail_other_charge, order_detail_discount, order_detail_cart_grand_total;

    TextView order_detail_delivery_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_detail_status = (TextView) findViewById(R.id.order_detail_status);
        order_detail_grand_total = (TextView) findViewById(R.id.order_detail_grand_total);

        order_detail_id = (TextView) findViewById(R.id.order_detail_id);
        order_detail_full_name = (TextView) findViewById(R.id.order_detail_full_name);
        order_detail_address = (TextView) findViewById(R.id.order_detail_address);
        order_detail_phone = (TextView) findViewById(R.id.order_detail_phone);

        order_detail_delivery_notes = (TextView) findViewById(R.id.order_detail_delivery_notes);

        order_detail_sub_total = (TextView) findViewById(R.id.order_detail_sub_total);
        order_detail_delivery_charge = (TextView) findViewById(R.id.order_detail_delivery_charge);
        order_detail_other_charge = (TextView) findViewById(R.id.order_detail_other_charge);
        order_detail_discount = (TextView) findViewById(R.id.order_detail_discount);
        order_detail_cart_grand_total = (TextView) findViewById(R.id.order_detail_cart_grand_total);

        order_detail_status.setText(getIntent().getStringExtra("status"));
        order_detail_grand_total.setText(getIntent().getStringExtra("grandTotal"));

        order_detail_id.setText(getIntent().getStringExtra("orderId"));
        order_detail_full_name.setText(getIntent().getStringExtra("userName"));
        order_detail_address.setText(getIntent().getStringExtra("userAddress"));
        order_detail_phone.setText(getIntent().getStringExtra("userPhone"));

        order_detail_delivery_notes.setText(getIntent().getStringExtra("notes"));

        order_detail_sub_total.setText(getIntent().getStringExtra("subTotal"));
        order_detail_delivery_charge.setText(getIntent().getStringExtra("deliveryCharge"));
        order_detail_other_charge.setText(getIntent().getStringExtra("othersCharge"));
        order_detail_discount.setText(getIntent().getStringExtra("discount"));
        order_detail_cart_grand_total.setText(getIntent().getStringExtra("grandTotal"));

//        if (getIntent().getStringExtra("status").equals("Processing")){
//            order_detail_status.setTextColor(getResources().getColor(R.color.white));
//        }
//        else if (getIntent().getStringExtra("status").equals("Shipped")){
//            order_detail_status.setTextColor(getResources().getColor(R.color.shipped));
//        }
//        else if (getIntent().getStringExtra("status").equals("Delivered")){
//            order_detail_status.setTextColor(getResources().getColor(R.color.delivered));
//        }
//        else
//            order_detail_status.setTextColor(getResources().getColor(R.color.cancelled));
    }
}
