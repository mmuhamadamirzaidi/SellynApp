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
import com.mmuhamadamirzaidi.sellynapp.Model.Category;
import com.mmuhamadamirzaidi.sellynapp.Model.Product;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.CategoryViewHolder;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference product;

    ImageView product_image;

    TextView product_name, product_notification;

    String categoryId="";

    RecyclerView recycler_product;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

    FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;

    List<String> suggestionList = new ArrayList<>();

    MaterialSearchBar product_list_search_bar;

    SwipeRefreshLayout swipe_layout_product_list;

    //Wishlist
    Database wishlistDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = findViewById(R.id.product_list_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        product = database.getReference("Product");

        //Local wishlist database
        wishlistDB = new Database(this);

        swipe_layout_product_list = (SwipeRefreshLayout) findViewById(R.id.swipe_layout_product_list);
        swipe_layout_product_list.setColorSchemeResources(R.color.colorPrimaryDark);

        swipe_layout_product_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Get CategoryId intent
                if (getIntent() != null){
                    categoryId = getIntent().getStringExtra("categoryId");
                }
                if (!categoryId.isEmpty() && categoryId != null){

                    if (Common.isConnectedToInternet(getBaseContext())){
                        loadProduct(categoryId);
                    }
                    else{
                        Toast.makeText(ProductListActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        swipe_layout_product_list.post(new Runnable() {
            @Override
            public void run() {
                // Get CategoryId intent
                if (getIntent() != null){
                    categoryId = getIntent().getStringExtra("categoryId");
                }
                if (!categoryId.isEmpty() && categoryId != null){

                    if (Common.isConnectedToInternet(getBaseContext())){
                        loadProduct(categoryId);
                    }
                    else{
                        Toast.makeText(ProductListActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Load category
        recycler_product = (RecyclerView) findViewById(R.id.recycler_product);
//        recycler_product.setHasFixedSize(true); //Need to remove if using Firebase Recycler Adapter/Disable for API 19 and below
        layoutManager = new LinearLayoutManager(this);
        recycler_product.setLayoutManager(layoutManager);



        product_list_search_bar = (MaterialSearchBar) findViewById(R.id.product_list_search_bar);
        product_list_search_bar.setHint("Search the products...");

        loadSuggestion();

        product_list_search_bar.setLastSuggestions(suggestionList);
        product_list_search_bar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //When search bar typing
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestionList){
                    if (search.toLowerCase().contains(product_list_search_bar.getText().toLowerCase().trim()))
                        suggest.add(search);
                }
                product_list_search_bar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        product_list_search_bar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar closed, restore original list
                if (!enabled)
                    recycler_product.setAdapter(adapter);
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

        searchAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class, R.layout.item_products, ProductViewHolder.class, product.orderByChild("productName").equalTo(text.toString().trim())) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.product_name.setText(model.getProductName());
                viewHolder.product_notification.setText(model.getNotificationNo());

                Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.product_image);

                final Product clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(ProductListActivity.this, "Product Name: "+clickItem.getProductName()+". Notification No: "+clickItem.getNotificationNo(), Toast.LENGTH_SHORT).show();

                        Intent product_detail = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                        product_detail.putExtra("productId", searchAdapter.getRef(position).getKey());
                        startActivity(product_detail);
                    }
                });
            }
        };
        recycler_product.setAdapter(searchAdapter);
    }

    private void loadSuggestion() {

        product.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Product item = dataSnapshot1.getValue(Product.class);
                    suggestionList.add(item.getProductName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProduct(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class, R.layout.item_products, ProductViewHolder.class, product.orderByChild("categoryId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(final ProductViewHolder viewHolder, final Product model, final int position) {

                viewHolder.product_name.setText(model.getProductName());
                viewHolder.product_notification.setText(model.getNotificationNo());

                Picasso.with(getBaseContext()).load(model.getProductImage()).into(viewHolder.product_image);

                //Add wishlist
                if (wishlistDB.currentWishlist(adapter.getRef(position).getKey()))
                    viewHolder.product_wishlist.setImageResource(R.drawable.ic_bookmark_primary_dark_24dp);

                //Remove wishlist
                viewHolder.product_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!wishlistDB.currentWishlist(adapter.getRef(position).getKey())){
                            wishlistDB.addToWishlist(adapter.getRef(position).getKey());
                            viewHolder.product_wishlist.setImageResource(R.drawable.ic_bookmark_primary_dark_24dp);
                            Toast.makeText(ProductListActivity.this, model.getProductName()+" added to wishlist!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            wishlistDB.clearWishlist(adapter.getRef(position).getKey());
                            viewHolder.product_wishlist.setImageResource(R.drawable.ic_bookmark_border_primary_dark_24dp);
                            Toast.makeText(ProductListActivity.this, model.getProductName()+" removed from wishlist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final Product clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(ProductListActivity.this, "Product Name: "+clickItem.getProductName()+". Notification No: "+clickItem.getNotificationNo(), Toast.LENGTH_SHORT).show();

                        Intent product_detail = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                        product_detail.putExtra("productId", adapter.getRef(position).getKey());
//                        product_detail.putExtra("notificationNo", model.getNotificationNo());
                        startActivity(product_detail);
                    }
                });
            }
        };
        recycler_product.setAdapter(adapter);
        swipe_layout_product_list.setRefreshing(false);
    }
}
