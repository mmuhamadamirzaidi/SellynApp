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
import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.Model.BannedProduct;
import com.mmuhamadamirzaidi.sellynapp.Model.Holder;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.BannedProductViewHolder;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.HolderViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HolderListActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference holder;

    RecyclerView recycler_holder;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Holder, HolderViewHolder> adapter;
    FirebaseRecyclerAdapter<Holder, HolderViewHolder> searchAdapter;

    List<String> suggestionList = new ArrayList<>();

    MaterialSearchBar holder_list_search_bar;

    SwipeRefreshLayout swipe_layout_holder_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder_list);

        Toolbar toolbar = findViewById(R.id.holder_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Swipe Layout
        swipe_layout_holder_list = (SwipeRefreshLayout) findViewById(R.id.swipe_layout_holder_list);
        swipe_layout_holder_list.setColorSchemeResources(R.color.colorPrimaryDark);

        swipe_layout_holder_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext())){
                    loadHolder();
                }
                else{
                    Toast.makeText(getBaseContext(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Default, load for first time
        swipe_layout_holder_list.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext())){
                    loadHolder();
                }
                else{
                    Toast.makeText(getBaseContext(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        holder = database.getReference("Holder");

        // Load category
        recycler_holder = (RecyclerView) findViewById(R.id.recycler_holder);
//        recycler_holder.setHasFixedSize(true); //Need to remove if using Firebase Recycler Adapter/Disable for API 19 and below
        layoutManager = new LinearLayoutManager(this);
        recycler_holder.setLayoutManager(layoutManager);

        holder_list_search_bar = (MaterialSearchBar) findViewById(R.id.holder_list_search_bar);
        holder_list_search_bar.setHint("Search the banned products...");

        loadSuggestion();

        holder_list_search_bar.setLastSuggestions(suggestionList);
        holder_list_search_bar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //When search bar typing
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestionList){
                    if (search.toLowerCase().contains(holder_list_search_bar.getText().toLowerCase().trim()))
                        suggest.add(search);
                }
                holder_list_search_bar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder_list_search_bar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar closed, restore original list
                if (!enabled)
                    recycler_holder.setAdapter(adapter);
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

        searchAdapter = new FirebaseRecyclerAdapter<Holder, HolderViewHolder>(Holder.class, R.layout.item_holder, HolderViewHolder.class, holder.orderByChild("manufacturerName").equalTo(text.toString().trim())) {
            @Override
            protected void populateViewHolder(HolderViewHolder viewHolder, Holder model, int position) {
                viewHolder.holder_name.setText(model.getManufacturerName());
                viewHolder.holder_phone.setText(model.getManufacturerPhoneNo());

                Picasso.with(getBaseContext()).load(model.getManufacturerImage()).into(viewHolder.holder_image);

                final Holder clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

//                        Toast.makeText(BannedProductListActivity.this, "Product Name: "+clickItem.getProductName()+". Notification No: "+clickItem.getNotificationNo(), Toast.LENGTH_SHORT).show();

                        Intent product_detail = new Intent(HolderListActivity.this, HolderDetailActivity.class);
                        product_detail.putExtra("holderId", searchAdapter.getRef(position).getKey());
                        startActivity(product_detail);
                    }
                });
            }
        };
        recycler_holder.setAdapter(searchAdapter);
    }

    private void loadSuggestion() {

        holder.orderByChild("holderId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Holder item = dataSnapshot1.getValue(Holder.class);
                    suggestionList.add(item.getManufacturerName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadHolder() {

        adapter = new FirebaseRecyclerAdapter<Holder, HolderViewHolder>(Holder.class, R.layout.item_holder, HolderViewHolder.class, holder) {
            @Override
            protected void populateViewHolder(HolderViewHolder viewHolder, Holder model, int position) {
                viewHolder.holder_name.setText(model.getManufacturerName());
                viewHolder.holder_phone.setText(model.getManufacturerPhoneNo());

                Picasso.with(getBaseContext()).load(model.getManufacturerImage()).into(viewHolder.holder_image);

                final Holder clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

//                        // Get CategoryId and send to new activity
                        Intent product_id = new Intent(HolderListActivity.this, HolderDetailActivity.class);
                        product_id.putExtra("holderId", adapter.getRef(position).getKey());
                        startActivity(product_id);

//                        Toast.makeText(BannedProductListActivity.this, "Test Banned Product : "+adapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recycler_holder.setAdapter(adapter);
        swipe_layout_holder_list.setRefreshing(false);
    }
}
