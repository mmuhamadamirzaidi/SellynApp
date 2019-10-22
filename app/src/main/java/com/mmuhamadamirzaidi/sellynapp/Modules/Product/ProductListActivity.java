package com.mmuhamadamirzaidi.sellynapp.Modules.Product;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
import com.mmuhamadamirzaidi.sellynapp.Modules.Cart.CartActivity;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Database.Database;
import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.Modules.General.MainActivity;
import com.mmuhamadamirzaidi.sellynapp.Model.Product;
import com.mmuhamadamirzaidi.sellynapp.Modules.Account.OrderStatusActivity;
import com.mmuhamadamirzaidi.sellynapp.R;
import com.mmuhamadamirzaidi.sellynapp.Modules.Account.SettingActivity;
import com.mmuhamadamirzaidi.sellynapp.Modules.General.SignInActivity;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ProductListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

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

    ImageView header_profile_image;

    TextView header_fullname, header_identity_card;

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

            Intent menuIntent = new Intent(ProductListActivity.this, MainActivity.class);
            startActivity(menuIntent);

        }
//        else if (id == R.id.nav_holders) {
//
////            Toast.makeText(MainActivity.this, "Holders", Toast.LENGTH_SHORT).show();
//            Intent holderIntent = new Intent(MainActivity.this, HolderListActivity.class);
//            startActivity(holderIntent);
//
//        }
        else if (id == R.id.nav_cart) {

            Intent menuIntent = new Intent(ProductListActivity.this, CartActivity.class);
            startActivity(menuIntent);

        } else if (id == R.id.nav_wishlist) {

            Toast.makeText(ProductListActivity.this, "Wishlist", Toast.LENGTH_SHORT).show();

        }
//        else if (id == R.id.banned_products) {
//
////            Toast.makeText(MainActivity.this, "Banned Products", Toast.LENGTH_SHORT).show();
//            Intent bannedProductIntent = new Intent(MainActivity.this, BannedProductListActivity.class);
//            startActivity(bannedProductIntent);
//
//        }
        else if (id == R.id.news) {

            Toast.makeText(ProductListActivity.this, "News", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_account) {

            Toast.makeText(ProductListActivity.this, "Account", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_settings) {

            Intent settingIntent = new Intent(ProductListActivity.this, SettingActivity.class);
            startActivity(settingIntent);

        }else if (id == R.id.nav_sign_out) {

            //Forget user information
            Paper.book().destroy();

            Intent menuIntent = new Intent(ProductListActivity.this, SignInActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(menuIntent);
            Toast.makeText(ProductListActivity.this, "Sign out successfully", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
