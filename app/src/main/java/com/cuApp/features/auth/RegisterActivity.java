package com.cuApp.features.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cuApp.R;
import com.cuApp.features.dashboard.DashboardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    //adding views
    EditText mEmailEt, mPasswordEt;
    ImageView mmainIm;
    Button mRegisterBtn;
    TextView mHaveAccount;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Actionbar and App title

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        //enable button to get back
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init

        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mRegisterBtn = findViewById(R.id.register_btn);
        mHaveAccount = findViewById(R.id.have_account);
        mmainIm = findViewById(R.id.mainIm);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        //register button click
        mRegisterBtn.setOnClickListener(v -> {
            // imput pass and email
            String email = mEmailEt.getText().toString().trim();
            String password = mPasswordEt.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmailEt.setError("Invalid Email");
                mEmailEt.setFocusable(true);
            } else if (password.length() < 6) {
                mPasswordEt.setError("Password Too Short, minimum 6 characters");
                mPasswordEt.setFocusable(true);
            } else {
                registerUser(email, password); //register user
            }
        });

        //In case have already an account

        mHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, AuthActivity.class));
            finish();
        });

    }

    private void registerUser(String email, String password) {
        //if all correct, register user after progressing bar
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();
                        //get user email uid from auth
                        String email1 = user.getEmail();
                        String uid = user.getUid();
                        //HashMap to store data when registering
                        HashMap<Object, String> hashMap = new HashMap<>();
                        //put info on hashmap
                        hashMap.put("email", email1);
                        hashMap.put("uid", uid);
                        hashMap.put("name", "");
                        hashMap.put("college", "");
                        hashMap.put("course", "");
                        hashMap.put("about", "");
                        hashMap.put("intsub", "");
                        hashMap.put("image", "");
                        //firebase instance
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //path to store user date
                        DatabaseReference reference = database.getReference("Users");
                        //put data in database
                        reference.child(uid).setValue(hashMap);

                        Toast.makeText(RegisterActivity.this, "Registered...\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                        finish();
                    } else {

                        // If sign in fails, no dialog and start activity
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
