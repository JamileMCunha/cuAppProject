package com.cuApp.features.dashboard.chat;

import com.cuApp.features.auth.UserModel;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class MessageModel implements IMessage {

    private String id;
    private String text;
    private UserModel userModel;
    private Date createdAt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return userModel;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public static MessageModel of(String id, String text, UserModel userModel, Date createdAt) {
        MessageModel model = new MessageModel();
        model.id = id;
        model.text = text;
        model.userModel = userModel;
        model.createdAt = createdAt;
        return model;
    }

    public static  MessageModel from(Message message) {
        MessageModel model = new MessageModel();
        model.id = message.getUid();
        model.text = message.getText();
        model.userModel = UserModel.of(message.getUserUid(), message.getUserName(), message.getUserImage());
        model.createdAt = new Date(message.getTimestamp());
        return model;
    }
}
