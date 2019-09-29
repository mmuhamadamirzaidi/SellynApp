package com.mmuhamadamirzaidi.sellynapp;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Database.Database;
import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.Model.BannedProduct;
import com.mmuhamadamirzaidi.sellynapp.Model.Category;
import com.mmuhamadamirzaidi.sellynapp.Model.Product;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.BannedProductViewHolder;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.CategoryViewHolder;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BannedProductListActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference bannedproduct;

    RecyclerView recycler_banned_product;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<BannedProduct, BannedProductViewHolder> adapter;
    FirebaseRecyclerAdapter<BannedProduct, BannedProductViewHolder> searchAdapter;

    List<String> suggestionList = new ArrayList<>();

    MaterialSearchBar banned_product_list_search_bar;

    SwipeRefreshLayout swipe_layout_banned_product_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned_product_list);

        Toolbar toolbar = findViewById(R.id.product_banned_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Swipe Layout
        swipe_layout_banned_product_list = (SwipeRefreshLayout) findViewById(R.id.swipe_layout_banned_product_list);
        swipe_layout_banned_product_list.setColorSchemeResources(R.color.colorPrimaryDark);

        swipe_layout_banned_product_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext())){
                    loadBannedProduct();
                }
                else{
                    Toast.makeText(getBaseContext(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Default, load for first time
        swipe_layout_banned_product_list.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext())){
                    loadBannedProduct();
                }
                else{
                    Toast.makeText(getBaseContext(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        bannedproduct = database.getReference("BannedProduct");

        // Load category
        recycler_banned_product = (RecyclerView) findViewById(R.id.recycler_banned_product);
//        recycler_banned_product.setHasFixedSize(true); //Need to remove if using Firebase Recycler Adapter/Disable for API 19 and below
        layoutManager = new LinearLayoutManager(this);
        recycler_banned_product.setLayoutManager(layoutManager);

        banned_product_list_search_bar = (MaterialSearchBar) findViewById(R.id.banned_product_list_search_bar);
        banned_product_list_search_bar.setHint("Search the banned products...");

        loadSuggestion();

        banned_product_list_search_bar.setLastSuggestions(suggestionList);
        banned_product_list_search_bar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //When search bar typing
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestionList){
                    if (search.toLowerCase().contains(banned_product_list_search_bar.getText().toLowerCase().trim()))
                        suggest.add(search);
                }
                banned_product_list_search_bar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        banned_product_list_search_bar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar closed, restore original list
                if (!enabled)
                    recycler_banned_product.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When finish. show result
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<BannedProduct, BannedProductViewHolder>(BannedProduct.class, R.layout.item_banned_product, BannedProductViewHolder.class, bannedproduct.orderByChild("productName").equalTo(text.toString().trim())) {
            @Override
            protected void populateViewHolder(BannedProductViewHolder viewHolder, BannedProduct model, int position) {
                viewHolder.banned_product_name.setText(model.getProductName());
                viewHolder.banned_product_notification_no.setText(model.getNotificationNo());

                Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.banned_product_image);

                final BannedProduct clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

//                        Toast.makeText(BannedProductListActivity.this, "Product Name: "+clickItem.getProductName()+". Notification No: "+clickItem.getNotificationNo(), Toast.LENGTH_SHORT).show();

                        Intent product_detail = new Intent(BannedProductListActivity.this, BannedProductDetailActivity.class);
                        product_detail.putExtra("bannedProductId", searchAdapter.getRef(position).getKey());
                        startActivity(product_detail);
                    }
                });
            }
        };
        recycler_banned_product.setAdapter(searchAdapter);
    }

    private void loadSuggestion() {

        bannedproduct.orderByChild("bannedProductId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    BannedProduct item = dataSnapshot1.getValue(BannedProduct.class);
                    suggestionList.add(item.getProductName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadBannedProduct() {

        adapter = new FirebaseRecyclerAdapter<BannedProduct, BannedProductViewHolder>(BannedProduct.class, R.layout.item_banned_product, BannedProductViewHolder.class, bannedproduct) {
            @Override
            protected void populateViewHolder(BannedProductViewHolder viewHolder, BannedProduct model, int position) {
                viewHolder.banned_product_name.setText(model.getProductName());
                viewHolder.banned_product_notification_no.setText(model.getNotificationNo());

                Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.banned_product_image);

                final BannedProduct clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

//                        // Get CategoryId and send to new activity
                        Intent product_id = new Intent(BannedProductListActivity.this, BannedProductDetailActivity.class);
                        product_id.putExtra("bannedProductId", adapter.getRef(position).getKey());
                        startActivity(product_id);

//                        Toast.makeText(BannedProductListActivity.this, "Test Banned Product : "+adapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recycler_banned_product.setAdapter(adapter);
        swipe_layout_banned_product_list.setRefreshing(false);
    }


}
