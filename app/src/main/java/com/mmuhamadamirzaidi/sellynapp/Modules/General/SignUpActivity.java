package com.mmuhamadamirzaidi.sellynapp.Modules.General;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Model.User;
import com.mmuhamadamirzaidi.sellynapp.R;

import dmax.dialog.SpotsDialog;

public class SignUpActivity extends AppCompatActivity {

    private EditText sign_up_fullname, sign_up_phone, sign_up_password;
    private Button button_sign_up, button_account_sign_in;

    private String sign_up_identity_card = "961122115566";

    private String sign_up_address = "Semarak Beach Inn Dungun, Kampung Sura Atas, Kuala Dungun, Terengganu";
    private String sign_up_image = "https://firebasestorage.googleapis.com/v0/b/sellynapp.appspot.com/o/profile.png?alt=media&token=f7e7b751-bc48-4d47-b214-bde3aa30df24";
    private String sign_up_is_staff = "false";
    private String sign_up_secure_code = "abc123456";
    private String sign_up_holder_id = "00";

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Hide the Status Bar and the Navigation Bar

        View overlay = findViewById(R.id.activity_sign_up);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        sign_up_fullname = findViewById(R.id.sign_up_fullname);
        sign_up_phone = findViewById(R.id.sign_up_phone);
        sign_up_password = findViewById(R.id.sign_up_password);

        button_sign_up = findViewById(R.id.button_sign_up);
        button_account_sign_in = findViewById(R.id.button_account_sign_in);

        // Custom dialog
        dialog = new SpotsDialog.Builder().setContext(SignUpActivity.this).setTheme(R.style.SignUp).build();

        // Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    dialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // Check if user exist
                            if (dataSnapshot.child(sign_up_phone.getText().toString().trim()).exists()) {

                                dialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "Phone number already exist!", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();

                                // Get user information
                                User user = new User(sign_up_fullname.getText().toString().trim(), sign_up_password.getText().toString().trim(), sign_up_identity_card, sign_up_phone.getText().toString().trim(), sign_up_address, sign_up_image, sign_up_is_staff, sign_up_secure_code, sign_up_holder_id);
                                table_user.child(sign_up_phone.getText().toString().trim()).setValue(user);

                                Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                                SendUserToSignInActivity();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
//                    return;
                }
            }
        });

        button_account_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInActivity = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(signInActivity);
            }
        });
    }

    private void SendUserToSignInActivity() {
        Intent mainIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
