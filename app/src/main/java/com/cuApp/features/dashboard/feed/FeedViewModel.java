package com.cuApp.features.dashboard.feed;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cuApp.core.Constants;
import com.cuApp.core.platform.BaseViewModel;
import com.cuApp.features.auth.AuthContext;
import com.cuApp.features.auth.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class FeedViewModel extends BaseViewModel {

    private DatabaseReference feedsRef;
    private ChildEventListener childEventListener;

    public FeedViewModel() {
        feedsRef = FirebaseDatabase.getInstance().getReference("feeds");
    }

    void observe(FeedsAdapter adapter) {
        childEventListener = feedsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Feed value = dataSnapshot.getValue(Feed.class);
                if (value == null) return;
                adapter.push(FeedModel.from(value));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Feed value = dataSnapshot.getValue(Feed.class);
                if (value == null) return;
                adapter.update(FeedModel.from(value));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Feed value = dataSnapshot.getValue(Feed.class);
                if (value == null) return;
                adapter.remove(FeedModel.from(value));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(Constants.TAG, "Can't listen to query " + childEventListener, databaseError.toException());
            }
        });
    }

    void submit(String text) {
        User user = AuthContext.getUser();
        DatabaseReference ref = feedsRef.push();
        ref.setValue(Feed.of(ref.getKey(), text, user.getImage(), user.getUid(), user.getName(), new Date().getTime()));
    }

    @Override
    protected void onCleared() {
        if (childEventListener != null) {
            feedsRef.removeEventListener(childEventListener);
        }
    }
}
