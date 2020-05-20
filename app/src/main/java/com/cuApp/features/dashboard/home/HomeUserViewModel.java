package com.cuApp.features.dashboard.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.cuApp.core.Constants;
import com.cuApp.features.auth.AuthContext;
import com.cuApp.features.auth.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeUserViewModel extends ViewModel {

    private DatabaseReference usersRef;

    public HomeUserViewModel() {
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    void observe(UserAdapter adapter) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                User me = AuthContext.getUser();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User value = child.getValue(User.class);
                    if (value == null || value.getUid().equals(me.getUid())) continue;
                    users.add(value);
                }
                adapter.setUsers(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(Constants.TAG, "Can't listen", databaseError.toException());
            }
        });
    }
}
