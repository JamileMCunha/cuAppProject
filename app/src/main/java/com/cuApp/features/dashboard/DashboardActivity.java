package com.cuApp.features.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentTransaction;

import com.cuApp.MainActivity;
import com.cuApp.R;
import com.cuApp.core.platform.BaseActivity;
import com.cuApp.features.auth.AuthContext;
import com.cuApp.features.auth.User;
import com.cuApp.features.dashboard.chat.DialogsFragment;
import com.cuApp.features.dashboard.feed.FeedsFragment;
import com.cuApp.features.dashboard.home.HomeUsersFragment;
import com.cuApp.features.dashboard.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends BaseActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        checkUserStatus();

        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        //bottom nav
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //profile fragment (default)
        ProfileFragment fragment1 = new ProfileFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //handle click menu
                    switch (menuItem.getItemId()) {
                        case R.id.nav_profile:
                            //profile fragment transaction
                            actionBar.setTitle("Profile");
                            ProfileFragment fragment1 = new ProfileFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, fragment1, "");
                            ft1.commit();
                            return true;
                        case R.id.nav_home:
                            //users/ home fragment transaction
                            actionBar.setTitle("Users");
                            HomeUsersFragment fragment2 = new HomeUsersFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "");
                            ft2.commit();
                            return true;
                        case R.id.nav_messages:
                            //messages fragment transaction
                            actionBar.setTitle("Messages");
                            DialogsFragment fragment3 = new DialogsFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3, "");
                            ft3.commit();
                            return true;
                        case R.id.nav_feed:
                            //feed fragment transaction
                            actionBar.setTitle("Feed");
                            FeedsFragment fragment4 = new FeedsFragment();
                            FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                            ft4.replace(R.id.content, fragment4, "");
                            ft4.commit();
                            return true;
                    }


                    return false;
                }
            };

    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
            return;
        }
        AuthContext.install(User.of(user.getUid(), user.getDisplayName(), user.getEmail()));
    }
}