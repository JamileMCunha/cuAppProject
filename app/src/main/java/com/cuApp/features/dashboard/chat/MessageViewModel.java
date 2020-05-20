package com.cuApp.features.dashboard.chat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cuApp.core.Constants;
import com.cuApp.core.platform.BaseViewModel;
import com.cuApp.features.auth.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

public class MessageViewModel extends BaseViewModel {

    private Query messagesQuery;
    private String lastMessage;

    private ChildEventListener childEventListener;
    private DialogRepository dialogRepository;

    public void setChat(String chat) {
        this.messagesQuery = FirebaseDatabase.getInstance().getReference("messages").child(chat);
        this.dialogRepository = new DialogRepository();
    }


    void observe(MessagesListAdapter<MessageModel> adapter) {
        childEventListener = messagesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message value = dataSnapshot.getValue(Message.class);
                if (value == null || value.getUid().equals(lastMessage)) return;
                adapter.addToStart(MessageModel.from(value), true);
                lastMessage = value.getUid();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message value = dataSnapshot.getValue(Message.class);
                if (value == null) return;
                adapter.update(MessageModel.from(value));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Message value = dataSnapshot.getValue(Message.class);
                if (value == null) return;
                adapter.delete(MessageModel.from(value));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(Constants.TAG, "Can't listen to query " + messagesQuery, databaseError.toException());
            }
        });
    }

    void submit(String text, User user, DialogModel dialogModel) {
        DatabaseReference ref = messagesQuery.getRef().push();
        Message message = Message.of(ref.getKey(), text, user.getUid(), user.getName(), user.getImage(), new Date().getTime());
        ref.setValue(message);

        dialogRepository.lastMessage(user.getUid(), dialogModel.getUsers().get(0).getId(), message);
    }

    @Override
    protected void onCleared() {
        if (messagesQuery != null && childEventListener != null) {
            messagesQuery.removeEventListener(childEventListener);
        }
    }
}
