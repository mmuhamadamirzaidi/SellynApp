package com.mmuhamadamirzaidi.sellynapp.Modules.Checkout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Common.Config;
import com.mmuhamadamirzaidi.sellynapp.Database.Database;
import com.mmuhamadamirzaidi.sellynapp.Model.Order;
import com.mmuhamadamirzaidi.sellynapp.Model.OrderRequest;
import com.mmuhamadamirzaidi.sellynapp.Modules.General.MainActivity;
import com.mmuhamadamirzaidi.sellynapp.R;
import com.mmuhamadamirzaidi.sellynapp.ViewHolder.CartAdapter;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    FirebaseDatabase database;
    DatabaseReference orderrequest;

    private EditText check_out_delivery_notes;
//    private EditText check_out_delivery_address, check_out_delivery_notes;
    private Button button_check_out;

    List<Order> cart = new ArrayList<>();
    List<Order> checkOutActivity = new ArrayList<>();

    CartAdapter cartAdapter;

    static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    private int PAYPAL_REQUEST_CODE = 9999;

    Place shippingAddress;

    //Location
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final int UPDATE_INTERVAL = 5000;
    private static final int FASTEST_INTERVAL = 3000;
    private static final int DISPLACEMENT = 10;

    private static final int LOCATION_REQUEST_CODE = 9999;
    private static final int PLAY_SERVICES_REQUEST = 9997;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        // Hide the Status Bar and the Navigation Bar

//        View overlay = findViewById(R.id.activity_check_out);
//
//        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //Runtime permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST_CODE);
        }
        else {
            if (checkPlayServices()){
                buildGoogleApiClient();
                createLocationRequest();
            }
        }

        Toolbar toolbar = findViewById(R.id.check_out_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

//        check_out_delivery_address = findViewById(R.id.check_out_delivery_address);
        check_out_delivery_notes = findViewById(R.id.check_out_delivery_notes);
        button_check_out = findViewById(R.id.button_check_out);

//        check_out_delivery_address.getText().toString().trim();
        PlaceAutocompleteFragment place_autocomplete_fragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //Set search icon before fragment
        place_autocomplete_fragment.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);

        //Set hint for auto complete
        ((EditText)place_autocomplete_fragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setHint("Insert your address");

        //Set text size
        ((EditText)place_autocomplete_fragment.getView().findViewById(R.id.place_autocomplete_search_input))
                .setTextSize(15);

        //Get address from autocomplete
        place_autocomplete_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                shippingAddress = place;
            }

            @Override
            public void onError(Status status) {
                Log.e("Error : ",status.getStatusMessage());
            }
        });

        //Init Paypal
        final Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        orderrequest = database.getReference("OrderRequest");

        checkOutActivity = new Database(this).getCart();
        cartAdapter = new CartAdapter(checkOutActivity, this);

        button_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cart_grand_total = Common.cart_grand_total_global.replace("RM","").replace(",", "");

                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(cart_grand_total), "MYR", "Sellyn App Order", PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent_payment = new Intent(getApplicationContext(), PaymentActivity.class);
                intent_payment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent_payment.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                startActivityForResult(intent_payment, PAYPAL_REQUEST_CODE);


                //Remove autocomplete address fragment
                getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment))
                        .commit();
            }
        });
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_REQUEST).show();
            else{
                Toast.makeText(this, "This device is not supported!", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case LOCATION_REQUEST_CODE:
            {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (checkPlayServices()){
                        buildGoogleApiClient();
                        createLocationRequest();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null){
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetail);

                        OrderRequest orderRequest = new OrderRequest(
                                Common.currentUser.getUserPhone(),
                                Common.currentUser.getUserName(),

                                shippingAddress.getAddress().toString().trim(),

                                Common.cart_grand_total_global.replace("RM","").replace(",", ""),

                                "0",

                                Common.cart_sub_total_global.replace("RM","").replace(",", ""),
                                Common.cart_delivery_charge_global.replace("RM","").replace(",", ""),
                                Common.cart_others_charge_global.replace("RM","").replace(",", ""),
                                Common.cart_discount_global.replace("RM","").replace(",", ""),

                                check_out_delivery_notes.getText().toString().trim(),

                                jsonObject.getJSONObject("response").getString("state"), //State from JSON
                                String.format("%s,%s", shippingAddress.getLatLng().latitude, shippingAddress.getLatLng().longitude),
                                cart
                        );

                        //Submit to Firebase
                        orderrequest.child(String.valueOf(System.currentTimeMillis()))
                                .setValue(orderRequest);

                        //Clear cart item

                        Common.cart_discount_global = "";
                        Common.cart_sub_total_global = "";
                        Common.cart_delivery_charge_global = "";
                        Common.cart_others_charge_global = "";
                        Common.cart_grand_total_global = "";

                        new Database(getBaseContext()).clearCart();

                        Toast.makeText(CheckOutActivity.this, "Thank you! All orders have been placed.", Toast.LENGTH_SHORT).show();
                        SendUserToMainActivity();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (requestCode == Activity.RESULT_CANCELED){
                Toast.makeText(CheckOutActivity.this, "Payment has been cancelled!", Toast.LENGTH_SHORT).show();
            }
            else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(CheckOutActivity.this, "Payment invalid!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(CheckOutActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            Log.d("LOCATION", "Current location : "+mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
        }
        else{
            Log.d("LOCATION", "Could not get current location");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }
}
