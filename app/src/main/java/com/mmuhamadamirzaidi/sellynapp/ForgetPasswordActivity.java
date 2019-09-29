package com.mmuhamadamirzaidi.sellynapp;

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

import dmax.dialog.SpotsDialog;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText forget_password_phone, forget_password_secure_code;
    private Button button_forget_password, button_account_sign_in;

    private AlertDialog dialog_loading;

    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forget_password_phone = findViewById(R.id.forget_password_phone);
        forget_password_secure_code = findViewById(R.id.forget_password_secure_code);

        button_forget_password = findViewById(R.id.button_forget_password);
        button_account_sign_in = findViewById(R.id.button_account_sign_in);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        // Custom dialog
        dialog_loading = new SpotsDialog.Builder().setContext(ForgetPasswordActivity.this).setTheme(R.style.Loading).build();

        button_account_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInActivity = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                startActivity(signInActivity);
            }
        });

        button_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_loading.show();

//                forget_password_phone.getText().toString().trim();
//                forget_password_secure_code.getText().toString().trim();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(forget_password_phone.getText().toString().trim()).exists()) {

                            dialog_loading.dismiss();
                            // Get user information
                            User user = dataSnapshot.child(forget_password_phone.getText().toString().trim()).getValue(User.class);

                            if (user.getUserSecureCode().equals(forget_password_secure_code.getText().toString().trim())) {

                                Toast.makeText(ForgetPasswordActivity.this, "Your password : "+user.getUserPassword(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "Wrong secure code!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            dialog_loading.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "User don't exist in system!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
