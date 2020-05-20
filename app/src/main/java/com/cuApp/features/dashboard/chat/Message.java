package com.cuApp.features.dashboard.chat;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class Message {

    private String uid;
    private String text;
    private String userUid;
    private String userName;
    private String userImage;
    private long timestamp;

    public Message() {
    }

    public String getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static Message of(String uid, String text, String userUid, String userName, String userImage, long timestamp) {
        Message message = new Message();
        message.uid = uid;
        message.text = text;
        message.userUid = userUid;
        message.userName = userName;
        message.userImage = userImage;
        message.timestamp = timestamp;
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return timestamp == message.timestamp &&
                Objects.equals(uid, message.uid) &&
                Objects.equals(text, message.text) &&
                Objects.equals(userUid, message.userUid) &&
                Objects.equals(userName, message.userName) &&
                Objects.equals(userImage, message.userImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, text, userUid, userName, userImage, timestamp);
    }
}
