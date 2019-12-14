package com.mmuhamadamirzaidi.sellynapp.Modules.Account;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmuhamadamirzaidi.sellynapp.Common.Common;
import com.mmuhamadamirzaidi.sellynapp.Modules.General.SignInActivity;
import com.mmuhamadamirzaidi.sellynapp.R;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {

    ImageView back, save_profile;
    EditText edit_identity_card, edit_secure_code, edit_current_password, edit_current_new_password, edit_confirm_password;

    FirebaseDatabase database;
    DatabaseReference table_user;
    AlertDialog dialog_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Hide the Status Bar and the Navigation Bar

//        View overlay = findViewById(R.id.activity_edit_profile);
//
//        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.edit_profile_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        back = findViewById(R.id.back);
        save_profile = findViewById(R.id.save_profile);

        edit_identity_card = findViewById(R.id.edit_identity_card);
        edit_secure_code = findViewById(R.id.edit_secure_code);
        edit_current_password = findViewById(R.id.edit_current_password);
        edit_current_new_password = findViewById(R.id.edit_current_new_password);
        edit_confirm_password = findViewById(R.id.edit_confirm_password);

        // Custom dialog
        dialog_loading = new SpotsDialog.Builder().setContext(EditProfileActivity.this).setTheme(R.style.Loading).build();

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_loading.show();

                //Check current password
                if (edit_current_password.getText().toString().trim().equals(Common.currentUser.getUserPassword())){

                    //Check new and confirm password
                    if (edit_current_new_password.getText().toString().trim().equals(edit_confirm_password.getText().toString().trim())){

                        Map<String, Object> updatePassword = new HashMap<>();
                        updatePassword.put("userPassword", edit_current_new_password.getText().toString().trim());

                        //Update to database
                        table_user.child(Common.currentUser.getUserPhone())
                                .updateChildren(updatePassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog_loading.dismiss();
                                        Toast.makeText(EditProfileActivity.this, "New password updated!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog_loading.dismiss();
                                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        dialog_loading.dismiss();
                        Toast.makeText(EditProfileActivity.this, "New password not match!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    dialog_loading.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Wrong current password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
