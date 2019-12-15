package com.mmuhamadamirzaidi.sellynapp.Modules.Account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.Model.OrderRequest;
import com.mmuhamadamirzaidi.sellynapp.Model.Product;
import com.mmuhamadamirzaidi.sellynapp.R;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.OrderViewHolder;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.ProductViewHolder;

public class OrderStatusActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference orderrequest;

    RecyclerView recycler_order, recycler_order_detail;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<OrderRequest, OrderViewHolder> adapter;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        // Hide the Status Bar and the Navigation Bar

//        View overlay = findViewById(R.id.activity_order_status);
//
//        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.order_list_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        orderrequest = database.getReference("OrderRequest");

        // Load category
        recycler_order = (RecyclerView) findViewById(R.id.recycler_order);
//        recycler_order.setHasFixedSize(true); //Need to remove if using Firebase Recycler Adapter/Disable for API 19 and below
        layoutManager = new LinearLayoutManager(this);
        recycler_order.setLayoutManager(layoutManager);

        loadOrder(Common.currentUser.getUserPhone());
    }

    private void loadOrder(String userPhone) {

        Query getOrderByUser = orderrequest.orderByChild("userPhone")
                .equalTo(userPhone);

        FirebaseRecyclerOptions<OrderRequest> orderOptions = new FirebaseRecyclerOptions.Builder<OrderRequest>()
                .setQuery(getOrderByUser, OrderRequest.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<OrderRequest, OrderViewHolder>(orderOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position, @NonNull final OrderRequest model) {

                viewHolder.item_order_id.setText(adapter.getRef(position).getKey());
//                viewHolder.item_order_date.setText(model.);
                viewHolder.item_order_price.setText(model.getGrandTotal());
                viewHolder.item_order_status.setText(convertCodeToStatus(model.getStatus()));

                if (model.getStatus().equals("0")){
                    viewHolder.item_order_status.setTextColor(getResources().getColor(R.color.textColorPrimary));
                }
                else if (model.getStatus().equals("1")){
                    viewHolder.item_order_status.setTextColor(getResources().getColor(R.color.shipped));
                }
                else if (model.getStatus().equals("2")){
                    viewHolder.item_order_status.setTextColor(getResources().getColor(R.color.delivered));
                }
                else
                    viewHolder.item_order_status.setTextColor(getResources().getColor(R.color.cancelled));


//                final OrderRequest clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {


                        Toast.makeText(OrderStatusActivity.this, "Order Id: "+adapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();

                        Intent order_detail = new Intent(OrderStatusActivity.this, OrderDetailActivity.class);

                        order_detail.putExtra("status", convertCodeToStatus(model.getStatus()));
                        order_detail.putExtra("orderId", adapter.getRef(position).getKey());

                        order_detail.putExtra("userName", model.getUserName());
                        order_detail.putExtra("userAddress", model.getUserAddress());
                        order_detail.putExtra("userPhone", model.getUserPhone());

                        order_detail.putExtra("notes", model.getNotes());

                        order_detail.putExtra("subTotal", model.getGrandSubTotal());
                        order_detail.putExtra("deliveryCharge", model.getGrandDeliveryCharge());
                        order_detail.putExtra("othersCharge", model.getGrandOthersCharge());
                        order_detail.putExtra("discount", model.getGrandDiscount());

                        order_detail.putExtra("grandTotal", model.getGrandTotal());

                        Common.currentRequest = model;
                        startActivity(order_detail);


                    }
                });

            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_products, viewGroup, false);
                return new OrderViewHolder(itemView);
            }
        };
        adapter.startListening();
        recycler_order.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private String convertCodeToStatus(String status) {

        if (status.equals("0"))
            return "Processing";
        else if (status.equals("1"))
            return "Shipped";
        else if (status.equals("2"))
            return "Delivered";
        else
            return "Cancelled";
    }
}
