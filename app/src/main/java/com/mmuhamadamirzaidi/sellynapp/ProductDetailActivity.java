package com.mmuhamadamirzaidi.sellynapp;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Database.Database;
import com.mmuhamadamirzaidi.sellynapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.sellynapp.Model.Order;
import com.mmuhamadamirzaidi.sellynapp.Model.Product;
import com.mmuhamadamirzaidi.sellynapp.Model.Rating;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity implements RatingDialogListener {

    FirebaseDatabase database;
    DatabaseReference product, rating;

    ImageView detail_image;

    TextView detail_product_name, detail_product_notification, detail_product_price, detail_product_description, detail_total_review, detail_give_review;

    LinearLayout detail_view_total_review;

    RatingBar detail_rating_bar;

    FloatingActionButton detail_product_fab_bookmark, detail_product_fab_cart;

    String productId="", quantity = "1";

    Product currentProduct;

    String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

//    Date time = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.product_detail_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        product = database.getReference("Product");
        rating = database.getReference("Rating");

        // Init view
        detail_image = (ImageView) findViewById(R.id.detail_image);

        detail_product_name = (TextView) findViewById(R.id.detail_product_name);
        detail_product_notification = (TextView) findViewById(R.id.detail_product_notification);
        detail_product_price = (TextView) findViewById(R.id.detail_product_price);
        detail_product_description = (TextView) findViewById(R.id.detail_product_description);
        detail_total_review = (TextView) findViewById(R.id.detail_total_review);


        detail_give_review = (TextView) findViewById(R.id.detail_give_review);

        detail_rating_bar = (RatingBar) findViewById(R.id.detail_rating_bar);

        detail_view_total_review = (LinearLayout) findViewById(R.id.detail_view_total_review);

        detail_product_fab_bookmark = (FloatingActionButton) findViewById(R.id.detail_product_fab_bookmark);
        detail_product_fab_cart = (FloatingActionButton) findViewById(R.id.detail_product_fab_cart);

        detail_give_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        detail_product_fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new Order(
                        productId,
                        currentProduct.getProductName(),
                        quantity,
                        currentProduct.getProductPrice(),
                        currentProduct.getProductDiscount(),
                        currentProduct.getProductImage()
                ));

                Toast.makeText(ProductDetailActivity.this, currentProduct.getProductName()+" added to cart!", Toast.LENGTH_SHORT).show();
            }
        });

        // Get productId intent
        if (getIntent() != null){
            productId = getIntent().getStringExtra("productId");
        }
        if (!productId.isEmpty() && productId != null){

            if (Common.isConnectedToInternet(this)){
                loadProductDetail(productId);
                loadProductRating(productId);
            }
            else{
                Toast.makeText(ProductDetailActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void loadProductRating(String productId) {

        Query productRating = rating.orderByChild("productId").equalTo(productId);

        productRating.addValueEventListener(new ValueEventListener() {

            int count = 0, sum = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    Rating item = dataSnapshot1.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRatingValue());
                    count++;
                }
                if (count != 0){
                    float average = sum/count;
                    detail_rating_bar.setRating(average);
                    detail_total_review.setText(String.valueOf(count));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Terrible", "Deficient", "Fair", "Great", "Incredible"))
                .setDefaultRating(1)
                .setTitle("Rate this product")
                .setTitleTextColor(R.color.textColorPrimary)
                .setDescription("Please give some stars and review about "+currentProduct.getProductName()+" product")
                .setDescriptionTextColor(R.color.textColorAccent)
                .setHint("Please write your review here...")
                .setHintTextColor(R.color.textColorAccent)
                .setCommentTextColor(R.color.textColorAccent)
                .setCommentBackgroundColor(R.color.backgroundApp)
                .setWindowAnimation(R.style.RatingDialogFadeAnimation)
//                .setCancelable(false)
//                .setCanceledOnTouchOutside(false)
                .create(ProductDetailActivity.this)
                .show();
    }

    private void loadProductDetail(String productId) {
        product.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProduct = dataSnapshot.getValue(Product.class);

                // Set image
                Picasso.with(getBaseContext()).load(currentProduct.getProductImage()).into(detail_image);

                detail_product_name.setText(currentProduct.getProductName());
                detail_product_notification.setText(currentProduct.getNotificationNo());
                detail_product_price.setText(currentProduct.getProductPrice());
                detail_product_description.setText(currentProduct.getProductDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, String review) {

        //Get rating and upload to Firebase
        final Rating currentRating = new Rating(productId, Common.currentUser.getUserPhone(), Common.currentUser.getUserImage(), Common.currentUser.getUserName(), time, String.valueOf(value), review);

        rating.child(Common.currentUser.getUserPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //If rating already exist
                if (dataSnapshot.child(Common.currentUser.getUserPhone()).exists()){

                    //Remove previous rating value (Boleh je delete haha, function useless)
                    rating.child(Common.currentUser.getUserPhone()).removeValue();

                    //Update new rating value
                    rating.child(Common.currentUser.getUserPhone()).setValue(currentRating);
                }
                else{
                    //Update new rating value
                    rating.child(Common.currentUser.getUserPhone()).setValue(currentRating);
                }
                Toast.makeText(ProductDetailActivity.this, "Thanks for review this product!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
