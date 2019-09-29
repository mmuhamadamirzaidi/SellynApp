package com.mmuhamadamirzaidi.sellynapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Model.User;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    private EditText sign_in_phone, sign_in_password;
    private Button button_sign_in, button_account_sign_up;

    private CheckBox sign_in_remember_me;

    private AlertDialog dialog, dialog_loading;

    FirebaseDatabase database;
    DatabaseReference table_user;

    private TextView sign_in_forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign_in_phone = findViewById(R.id.sign_in_phone);
        sign_in_password = findViewById(R.id.sign_in_password);

        button_sign_in = findViewById(R.id.button_sign_in);
        button_account_sign_up = findViewById(R.id.button_account_sign_up);

        sign_in_remember_me = findViewById(R.id.sign_in_remember_me);

        sign_in_forget_password = findViewById(R.id.sign_in_forget_password);

        sign_in_remember_me.setTypeface(ResourcesCompat.getFont(getBaseContext(), R.font.mr));

        // Custom dialog
        dialog = new SpotsDialog.Builder().setContext(SignInActivity.this).setTheme(R.style.SignIn).build();
        dialog_loading = new SpotsDialog.Builder().setContext(SignInActivity.this).setTheme(R.style.Loading).build();

        // Init Paper
        Paper.init(this);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        //Check remember information
        String remember_user_phone = Paper.book().read(Common.USER_PHONE_KEY);
        String remember_user_password = Paper.book().read(Common.USER_PASSWORD_KEY);

        if (remember_user_phone != null && remember_user_password != null) {

            if (!remember_user_phone.isEmpty() && !remember_user_password.isEmpty()) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    redirectSignIn(remember_user_phone, remember_user_password);
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
                }
            } else {
                SendUserToSignInActivity();
            }
        }

        sign_in_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetPasswordActivity = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                startActivity(forgetPasswordActivity);
            }
        });

        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                dialog.show();

                if (Common.isConnectedToInternet(getBaseContext())) {

                    //If checked, remember user phone and password
                    if (sign_in_remember_me.isChecked()) {
                        Paper.book().write(Common.USER_PHONE_KEY, sign_in_phone.getText().toString().trim());
                        Paper.book().write(Common.USER_PASSWORD_KEY, sign_in_password.getText().toString().trim());
                    }

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // Check if user exist
                            if (dataSnapshot.child(sign_in_phone.getText().toString().trim()).exists()) {

                                dialog.dismiss();
                                // Get user information
                                User user = dataSnapshot.child(sign_in_phone.getText().toString().trim()).getValue(User.class);
                                user.setUserPhone(sign_in_phone.getText().toString().trim());

                                if (user.getUserPassword().equals(sign_in_password.getText().toString().trim())) {
                                    Toast.makeText(SignInActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                    Common.currentUser = user;
                                    SendUserToMainActivity();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
                                Toast.makeText(SignInActivity.this, "User don't exist in system!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    dialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Please check Internet connection!", Toast.LENGTH_SHORT).show();
//                    return;
                }
            }
        });

        button_account_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpActivity = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(signUpActivity);
            }
        });
    }

    private void redirectSignIn(final String remember_user_phone, final String remember_user_password) {

        dialog_loading.show();

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Check if user exist
                if (dataSnapshot.child(remember_user_phone).exists()) {

                    dialog_loading.dismiss();
                    // Get user information
                    User user = dataSnapshot.child(remember_user_phone).getValue(User.class);
                    user.setUserPhone(remember_user_phone);

                    if (user.getUserPassword().equals(remember_user_password)) {
                        Toast.makeText(SignInActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                        Common.currentUser = user;

                        Intent splash = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(splash);
                        finish();

                    } else {
                        Toast.makeText(SignInActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                        SendUserToSignInActivity();
                    }
                } else {
                    dialog_loading.dismiss();
                    Toast.makeText(SignInActivity.this, "User don't exist in system!", Toast.LENGTH_SHORT).show();
                    SendUserToSignInActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void SendUserToSignInActivity() {
        Intent mainIntent = new Intent(SignInActivity.this, SignInActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
