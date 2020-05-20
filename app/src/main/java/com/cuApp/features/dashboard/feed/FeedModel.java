package com.cuApp.features.dashboard.feed;

import com.cuApp.features.auth.UserModel;

import java.util.Date;
import java.util.Objects;

public class FeedModel {

    private String id;
    private String text;
    private UserModel author;
    private Date createdAt;

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public UserModel getAuthor() {
        return author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedModel model = (FeedModel) o;
        return Objects.equals(id, model.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static FeedModel of(String id, String text, UserModel author, Date createdAt) {
        FeedModel model = new FeedModel();
        model.id = id;
        model.text = text;
        model.author = author;
        model.createdAt = createdAt;
        return model;
    }

    public static FeedModel from(Feed feed) {
        UserModel user = UserModel.of(feed.getUserUid(), feed.getUserName(), feed.getPhoto());
        return of(feed.getUid(), feed.getText(), user, new Date(feed.getTimestamp()));
    }
}
