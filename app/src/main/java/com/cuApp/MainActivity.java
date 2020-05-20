package com.cuApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.cuApp.features.auth.AuthActivity;
import com.cuApp.features.auth.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    //adding views

    Button mRegisterBtn, mLoginBtn;
    ImageView mlogoim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //init views
        mRegisterBtn = findViewById(R.id.register_btn);
        mLoginBtn = findViewById(R.id.login_btn);
        mlogoim = findViewById(R.id.logoim);

        //button click
        mRegisterBtn.setOnClickListener(v -> {
            //start RegisterActivity
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
        //button click login
        mLoginBtn.setOnClickListener(v -> {
            //start log in activity
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
        });
    }
}
