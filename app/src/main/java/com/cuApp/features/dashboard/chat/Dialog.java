package com.cuApp.features.dashboard.chat;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Dialog {

    private String uid;
    private String chat;
    private String name;
    private String photo;
    private String lastMessageUid;
    private String lastMessageText;
    private long lastMessageCreatedAt;

    public Dialog() {
    }

    public String getUid() {
        return uid;
    }

    public String getChat() {
        return chat;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getLastMessageUid() {
        return lastMessageUid;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public long getLastMessageCreatedAt() {
        return lastMessageCreatedAt;
    }

    public static Dialog of(String uid, String chat, String name, String photo) {
        Dialog dialog = new Dialog();
        dialog.uid = uid;
        dialog.chat = chat;
        dialog.name = name;
        dialog.photo = photo;
        return dialog;
    }

    public static Dialog of(String uid, String chat, String name, String photo, String lastMessageUid, String lastMessageText, long lastMessageCreatedAt) {
        Dialog dialog = of(uid, chat, name, photo);
        dialog.lastMessageUid = lastMessageUid;
        dialog.lastMessageText = lastMessageText;
        dialog.lastMessageCreatedAt = lastMessageCreatedAt;
        return dialog;
    }
}
