package com.cuApp.features.dashboard.chat;

import androidx.annotation.NonNull;

import com.cuApp.features.auth.AuthContext;
import com.cuApp.features.auth.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class DialogRepository {

    private FirebaseDatabase rootRef = FirebaseDatabase.getInstance();
    private DatabaseReference chatsRef = rootRef.getReference("chats");
    private DatabaseReference messagesRef = rootRef.getReference("messages");

    CompletableFuture<DialogModel> createDialog(User user) {
        User me = AuthContext.getUser();
        CompletableFuture<DialogModel> future = new CompletableFuture<>();
        chatsRef.child(me.getUid()).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dialog dialog = dataSnapshot.getValue(Dialog.class);
                if (dialog == null) {
                    dialog = create(user);
                }
                future.complete(DialogModel.from(dialog));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    void lastMessage(String user, String friend, Message message) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + user + "/" + friend + "/lastMessageUid", message.getUid());
        childUpdates.put("/" + user + "/" + friend + "/lastMessageText", message.getText());
        childUpdates.put("/" + user + "/" + friend + "/lastMessageCreatedAt", message.getTimestamp());
        childUpdates.put("/" + friend + "/" + user + "/lastMessageUid", message.getUid());
        childUpdates.put("/" + friend + "/" + user + "/lastMessageText", message.getText());
        childUpdates.put("/" + friend + "/" + user + "/lastMessageCreatedAt", message.getTimestamp());

        chatsRef.updateChildren(childUpdates);
    }

    private Dialog create(User user) {
        User me = AuthContext.getUser();

        String chat = messagesRef.push().getKey();

        DatabaseReference dialogMeRef = chatsRef.child(me.getUid()).child(user.getUid());
        Dialog dialog = Dialog.of(user.getUid(), chat, user.getName(), user.getImage());
        dialogMeRef.setValue(dialog);

        DatabaseReference dialogUserRef = chatsRef.child(user.getUid()).child(me.getUid());
        dialogUserRef.setValue(Dialog.of(me.getUid(), chat, me.getName(), me.getImage()));

        return dialog;
    }
}
