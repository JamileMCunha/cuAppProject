package com.cuApp.features.dashboard.chat;

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
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.function.Consumer;

public class DialogViewModel extends BaseViewModel {

    private DialogRepository dialogRepository;
    private DatabaseReference chats;
    private ChildEventListener childEventListener;

    public DialogViewModel() {
        String userUid = AuthContext.getUser().getUid();
        chats = FirebaseDatabase.getInstance().getReference("chats").child(userUid);
        dialogRepository = new DialogRepository();
    }

    void observe(DialogsListAdapter<DialogModel> adapter) {
        childEventListener = chats.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Dialog dialog = dataSnapshot.getValue(Dialog.class);
                if (dialog == null) return;
                adapter.addItem(DialogModel.from(dialog));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Dialog dialog = dataSnapshot.getValue(Dialog.class);
                if (dialog == null) return;
                adapter.updateItemById(DialogModel.from(dialog));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Dialog dialog = dataSnapshot.getValue(Dialog.class);
                if (dialog == null) return;
                adapter.deleteById(dialog.getUid());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(Constants.TAG, "Can't listen to query " + chats, databaseError.toException());
            }
        });
    }

    void createDialog(User user, Consumer<DialogModel> action) {
        dialogRepository.createDialog(user).thenAccept(action);
    }

    @Override
    protected void onCleared() {
        if (childEventListener != null) {
            chats.removeEventListener(childEventListener);
        }
    }
}
