package com.cuApp.features.dashboard.feed;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cuApp.R;
import com.cuApp.core.platform.BaseFragment;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageInput;

public class CommentsFragment extends BaseFragment {

    private FeedModel feedModel;
    private CommentViewModel commentViewModel;

    CommentsFragment(FeedModel feedModel) {
        this.feedModel = feedModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        commentViewModel.setFeed(feedModel.getId());

        initFeed(view);
        initList(view);
    }

    private void initFeed(View view) {
        ImageView avatar = view.findViewById(R.id.avatar);
        Picasso.get().load(feedModel.getAuthor().getAvatar()).placeholder(R.drawable.ic_facb).into(avatar);

        TextView feedBody = view.findViewById(R.id.feedBody);
        feedBody.setText(feedModel.getText());
    }

    private void initList(View view) {
        CommentsAdapter commentsAdapter = new CommentsAdapter(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.listComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commentsAdapter);

        commentViewModel.observe(commentsAdapter);

        view.findViewById(R.id.newComment).setOnClickListener(v -> newComment());
    }

    private void newComment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New feed");

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(12,12,12,12);

        MessageInput messageInput = new MessageInput(getContext()) {
            MessageInput init() {
                this.attachmentButton.setVisibility(GONE);
                this.messageInput.setHint("Enter a answer");
                this.messageSendButton.setImageResource(com.stfalcon.chatkit.R.drawable.ic_send);
                this.messageSendButton.setBackgroundResource(com.stfalcon.chatkit.R.drawable.mask_active);
                return this;
            }
        }.init();

        linearLayout.addView(messageInput);
        builder.setView(linearLayout);

        AlertDialog dialog = builder.show();

        messageInput.setInputListener(input -> {
            commentViewModel.submit(input.toString());
            dialog.dismiss();
            return true;
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_comments;
    }
}
