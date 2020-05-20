package com.cuApp.features.auth;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public final class AuthContext {

    private static User user;
    private static DatabaseReference usersRef;
    private static ValueEventListener valueEventListener;

    private AuthContext() {
        throw new UnsupportedOperationException("This class cannot be instantiate!");
    }

    public static User getUser() {
        return user;
    }

    public static void install(User user) {
        AuthContext.user = user;
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        valueEventListener = usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AuthContext.user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void uninstall() {
        if (usersRef != null) {
            usersRef.removeEventListener(valueEventListener);
        }
    }
}
