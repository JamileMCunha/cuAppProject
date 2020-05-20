package com.cuApp.features.dashboard.feed;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Feed {

    private String uid;
    private String text;
    private String photo;
    private String userUid;
    private String userName;
    private long timestamp;

    public Feed() {
    }

    public String getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public String getPhoto() {
        return photo;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getUserName() {
        return userName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static Feed of(String uid, String text, String photo, String userUid, String userName, long timestamp) {
        Feed feed = new Feed();
        feed.uid = uid;
        feed.text = text;
        feed.photo = photo;
        feed.userUid = userUid;
        feed.userName = userName;
        feed.timestamp = timestamp;
        return feed;
    }
}
