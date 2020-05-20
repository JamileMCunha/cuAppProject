package com.cuApp.features.dashboard.chat;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cuApp.R;
import com.cuApp.core.platform.BaseFragment;
import com.cuApp.features.auth.AuthContext;
import com.cuApp.features.auth.UserModel;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class MessagesFragment extends BaseFragment implements MessageInput.InputListener, MessagesListAdapter.SelectionListener {

    private MessagesListAdapter<MessageModel> messagesListAdapter;

    private UserModel sender = UserModel.from(AuthContext.getUser());

    private DialogModel dialogModel;

    private MessageViewModel messageViewModel;

    MessagesFragment(DialogModel dialogModel) {
        this.dialogModel = dialogModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter(view);
        initMessageInput(view);

        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.setChat(dialogModel.getChat());
        messageViewModel.observe(messagesListAdapter);
    }

    private void initAdapter(@NonNull View view) {
        messagesListAdapter = new MessagesListAdapter<>(sender.getId(), (imageView, url, payload) -> Picasso.get().load(url).into(imageView));
        messagesListAdapter.enableSelectionMode(this);

        MessagesList messagesList = view.findViewById(R.id.messagesList);
        messagesList.setAdapter(messagesListAdapter);
    }

    private void initMessageInput(@NonNull View view) {
        MessageInput input = view.findViewById(R.id.input);
        input.setInputListener(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_messages;
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        messageViewModel.submit(input.toString(), AuthContext.getUser(), dialogModel);
        return true;
    }

    @Override
    public void onSelectionChanged(int count) {

    }
}
