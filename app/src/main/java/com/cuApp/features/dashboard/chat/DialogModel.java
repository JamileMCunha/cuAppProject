package com.cuApp.features.dashboard.chat;

import com.cuApp.features.auth.UserModel;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DialogModel implements IDialog<MessageModel> {

    private String id;
    private String photo;
    private String name;
    private String chat;
    private List<UserModel> users;
    private MessageModel lastMessage;
    private int unreadCount;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return photo;
    }

    @Override
    public String getDialogName() {
        return name;
    }

    public String getChat() {
        return chat;
    }

    @Override
    public List<? extends IUser> getUsers() {
        return users;
    }

    @Override
    public MessageModel getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(MessageModel message) {
        this.lastMessage = message;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public static DialogModel from(Dialog dialog) {
        UserModel user = UserModel.of(dialog.getUid(), dialog.getName(), dialog.getPhoto());
        DialogModel model = new DialogModel();
        model.id = dialog.getUid();
        model.photo = dialog.getPhoto();
        model.name = dialog.getName();
        model.chat = dialog.getChat();
        model.users = Collections.singletonList(user);
        model.lastMessage = MessageModel.of(dialog.getLastMessageUid(), dialog.getLastMessageText(), user, new Date(dialog.getLastMessageCreatedAt()));
        return model;
    }
}
