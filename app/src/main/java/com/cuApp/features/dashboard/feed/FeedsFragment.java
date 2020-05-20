package com.cuApp.features.dashboard.feed;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cuApp.R;
import com.cuApp.core.platform.BaseFragment;
import com.stfalcon.chatkit.messages.MessageInput;

public class FeedsFragment extends BaseFragment {

    private FeedViewModel feedViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        initList(view);
    }

    private void initList(View view) {
        FeedsAdapter feedsAdapter = new FeedsAdapter(getContext());
        feedsAdapter.setOnItemClickListener(feedModel -> startFragment(new CommentsFragment(feedModel)));

        RecyclerView recyclerView = view.findViewById(R.id.listFeeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(feedsAdapter);

        feedViewModel.observe(feedsAdapter);

        view.findViewById(R.id.newFeed).setOnClickListener(v -> newFeed());
    }

    private void newFeed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New feed");

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(12,12,12,12);

        MessageInput messageInput = new MessageInput(getContext()) {
            MessageInput init() {
                this.attachmentButton.setVisibility(GONE);
                this.messageInput.setHint("Enter a question");
                this.messageSendButton.setImageResource(com.stfalcon.chatkit.R.drawable.ic_send);
                this.messageSendButton.setBackgroundResource(com.stfalcon.chatkit.R.drawable.mask_active);
                return this;
            }
        }.init();

        linearLayout.addView(messageInput);
        builder.setView(linearLayout);

        AlertDialog dialog = builder.show();

        messageInput.setInputListener(input -> {
            feedViewModel.submit(input.toString());
            dialog.dismiss();
            return true;
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_feed;
    }
}
