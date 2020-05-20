package com.cuApp.features.auth;

import com.stfalcon.chatkit.commons.models.IUser;

public class UserModel implements IUser {

    private String id;
    private String name;
    private String avatar;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public static UserModel from(User user) {
        UserModel model = new UserModel();
        model.id = user.getUid();
        model.name = user.getName();
        model.avatar = user.getImage();
        return model;
    }

    public static UserModel of(String id, String name, String avatar) {
        UserModel user = new UserModel();
        user.id = id;
        user.name = name;
        user.avatar = avatar;
        return user;
    }
}
