package com.mmuhamadamirzaidi.sellynapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Database.Database;
import com.mmuhamadamirzaidi.sellynapp.Helper.RecyclerItemTouchHelper;
import com.mmuhamadamirzaidi.sellynapp.Interface.RecyclerItemTouchHelperListener;
import com.mmuhamadamirzaidi.sellynapp.Model.Order;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.CartAdapter;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.CartViewHolder;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener, NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycler_cart;
    RecyclerView.LayoutManager layoutManager;

    TextView cart_add_new_product, cart_sub_total, cart_delivery_charge, cart_others_charge, cart_grand_total, cart_discount;

    Button cart_button_place_order;

    List<Order> cart = new ArrayList<>();

    CartAdapter cartAdapter;

    ImageView header_profile_image;

    TextView header_fullname, header_identity_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

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
        recycler_cart = (RecyclerView) findViewById(R.id.recycler_cart);
        recycler_cart.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);

        // Swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_cart);

        cart_add_new_product = (TextView) findViewById(R.id.cart_add_new_item);

        cart_sub_total = (TextView) findViewById(R.id.cart_sub_total);
        cart_delivery_charge = (TextView) findViewById(R.id.cart_delivery_charge);
        cart_others_charge = (TextView) findViewById(R.id.cart_others_charge);
        cart_grand_total = (TextView) findViewById(R.id.cart_grand_total);
        cart_discount = (TextView) findViewById(R.id.cart_discount);

        cart_button_place_order = (Button) findViewById(R.id.cart_button_place_order);


        cart_add_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        loadCartListProduct();

        // If cart have an item
        if (cart.size() > 0){

            cart_button_place_order.setEnabled(true);

            cart_button_place_order.setBackground(getResources().getDrawable(R.drawable.bgbtnsignin));
            cart_button_place_order.setTextColor(getResources().getColor(R.color.white));

            cart_button_place_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent checkoutIntent = new Intent(CartActivity.this, CheckOutActivity.class);
                    checkoutIntent.putExtra("cart_sub_total_global", Common.cart_sub_total_global);
                    checkoutIntent.putExtra("cart_delivery_charge_global", Common.cart_delivery_charge_global);
                    checkoutIntent.putExtra("cart_others_charge_global", Common.cart_others_charge_global);
                    checkoutIntent.putExtra("cart_discount_global", Common.cart_discount_global);
                    checkoutIntent.putExtra("cart_grand_total_global", Common.cart_grand_total_global);
                    startActivity(checkoutIntent);
                }
            });

        }
        else {

            cart_button_place_order.setEnabled(false);

            cart_button_place_order.setBackground(getResources().getDrawable(R.drawable.bgbtncreate));
            cart_button_place_order.setTextColor(getResources().getColor(R.color.textColorAccent));

        }
    }

    private void loadCartListProduct() {

        if (cart.size() > 0){
            cart_button_place_order.setEnabled(true);

            cart_button_place_order.setBackground(getResources().getDrawable(R.drawable.bgbtnsignin));
            cart_button_place_order.setTextColor(getResources().getColor(R.color.white));
        }
        else {
            cart_button_place_order.setEnabled(false);

            cart_button_place_order.setBackground(getResources().getDrawable(R.drawable.bgbtncreate));
            cart_button_place_order.setTextColor(getResources().getColor(R.color.textColorAccent));
        }

        cart = new Database(this).getCart();
        cartAdapter = new CartAdapter(cart, this);
        cartAdapter.notifyDataSetChanged(); //Detect any changes and update recycler view
        recycler_cart.setAdapter(cartAdapter);

        //Calculate grand total
        int sub_total_initial = 0, grand_total_initial = 0, delivery_charge = 0, others_charge = 0, discount = 0, total_charge;

        for (Order order:cart) {
            delivery_charge = (Integer.parseInt("6"));
            others_charge = (Integer.parseInt("1"));
            total_charge = delivery_charge + others_charge;

            discount += (Integer.parseInt(order.getDiscount()));

            sub_total_initial += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
            grand_total_initial = sub_total_initial + total_charge - discount;
        }

        Locale locale = new Locale("en", "MY");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        cart_sub_total.setText(fmt.format(sub_total_initial));
        cart_delivery_charge.setText(fmt.format(delivery_charge));
        cart_others_charge.setText(fmt.format(others_charge));
        cart_discount.setText(fmt.format(discount));
        cart_grand_total.setText(fmt.format(grand_total_initial));

        Common.cart_sub_total_global = (fmt.format(sub_total_initial));
        Common.cart_delivery_charge_global = (fmt.format(delivery_charge));
        Common.cart_others_charge_global = (fmt.format(others_charge));
        Common.cart_discount_global = (fmt.format(discount));
        Common.cart_grand_total_global = (fmt.format(grand_total_initial));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.DELETE_CART))
            clearCart(item.getOrder());

        return true;
    }

    private void clearCart(int position) {

        //Remove item at List<Order> by position
        cart.remove(position);

        //After that, delete all old data from SQLite
        new Database(this).clearCart();

        //Finally, update new item List<Order> to SQLite
        for (Order item:cart)
            new Database(this).addToCart(item);

        //Refresh cart list product
        loadCartListProduct();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
//        if (viewHolder instanceof CartViewHolder){
//            String name = ((CartAdapter)recycler_cart.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
//
//            Order deleteItem = ((CartAdapter)recycler_cart.getAdapter()).getItem(viewHolder.getAdapterPosition());
//            int deleteIndex = viewHolder.getAdapterPosition();
//
//            cartAdapter.removeItem(deleteIndex);
////            new Database(getBaseContext()).clearWishlist(deleteItem.getProductId(), Common.currentUser.getUserPhone());
//
//            //Calculate grand total
//            int sub_total_initial = 0, grand_total_initial = 0, delivery_charge = 0, others_charge = 0, discount = 0, total_charge;
////            List<Order> orders = new Database(getBaseContext()).getCart(Common.currentUser.getUserPhone());
//
//            for (Order order:cart) {
//                delivery_charge = (Integer.parseInt("6"));
//                others_charge = (Integer.parseInt("1"));
//                total_charge = delivery_charge + others_charge;
//
//                discount += (Integer.parseInt(order.getDiscount()));
//
//                sub_total_initial += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
//                grand_total_initial = sub_total_initial + total_charge - discount;
//            }
//
//            Locale locale = new Locale("en", "MY");
//            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//
//            cart_sub_total.setText(fmt.format(sub_total_initial));
//            cart_delivery_charge.setText(fmt.format(delivery_charge));
//            cart_others_charge.setText(fmt.format(others_charge));
//            cart_discount.setText(fmt.format(discount));
//            cart_grand_total.setText(fmt.format(grand_total_initial));
//        }
        Toast.makeText(this, "Test swipe delete function!", Toast.LENGTH_SHORT).show();
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

            Intent menuIntent = new Intent(CartActivity.this, MainActivity.class);
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

            Intent menuIntent = new Intent(CartActivity.this, CartActivity.class);
            startActivity(menuIntent);

        } else if (id == R.id.nav_order_status) {

            Intent menuIntent = new Intent(CartActivity.this, OrderStatusActivity.class);
            startActivity(menuIntent);

        }else if (id == R.id.nav_wishlist) {

            Toast.makeText(CartActivity.this, "Wishlist", Toast.LENGTH_SHORT).show();

        }
//        else if (id == R.id.banned_products) {
//
////            Toast.makeText(MainActivity.this, "Banned Products", Toast.LENGTH_SHORT).show();
//            Intent bannedProductIntent = new Intent(MainActivity.this, BannedProductListActivity.class);
//            startActivity(bannedProductIntent);
//
//        }
        else if (id == R.id.news) {

            Toast.makeText(CartActivity.this, "News", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_account) {

            Toast.makeText(CartActivity.this, "Account", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_settings) {

            Intent settingIntent = new Intent(CartActivity.this, SettingActivity.class);
            startActivity(settingIntent);

        }else if (id == R.id.nav_sign_out) {

            //Forget user information
            Paper.book().destroy();

            Intent menuIntent = new Intent(CartActivity.this, SignInActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(menuIntent);
            Toast.makeText(CartActivity.this, "Sign out successfully", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
