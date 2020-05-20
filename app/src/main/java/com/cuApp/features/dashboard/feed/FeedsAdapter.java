package com.cuApp.features.dashboard.feed;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.cuApp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedItem> {

    private Context context;
    private List<FeedModel> feeds = new ArrayList<>();
    private Consumer<FeedModel> clickListener;

    FeedsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FeedItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new FeedItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItem holder, int position) {
        holder.bind(feeds.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    void push(FeedModel feedModel) {
        feeds.add(feedModel);
        notifyItemInserted(feeds.size());
    }

    void update(FeedModel feedModel) {
        int position = findPosition(feedModel);
        if (position > -1) {
            feeds.set(position, feedModel);
            notifyItemChanged(position);
            return;
        }
        push(feedModel);
    }

    void remove(FeedModel feedModel) {
        int position = findPosition(feedModel);
        if (position > -1) {
            feeds.remove(position);
            notifyItemRemoved(position);
        }
    }

    private int findPosition(FeedModel feedModel) {
        int i = -1;
        for (FeedModel model : feeds) {
            i++;
            if (model.equals(feedModel)) {
                return i;
            }
        }
        return i;
    }

    void setOnItemClickListener(Consumer<FeedModel> listener) {
        this.clickListener = listener;
    }

    static class FeedItem extends RecyclerView.ViewHolder {

        private ImageView avatar;
        private TextView body;
        private TextView count;
        private TextView author;
        private TextView createdAt;
        private ImageButton newComment;

        FeedItem(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            body = itemView.findViewById(R.id.body);
            author = itemView.findViewById(R.id.author);
            createdAt = itemView.findViewById(R.id.createdAt);
            newComment = itemView.findViewById(R.id.icPlus);
        }

        void bind(FeedModel model, Consumer<FeedModel> listener) {
            if (model.getAuthor() != null && !TextUtils.isEmpty(model.getAuthor().getAvatar())) {
                Picasso.get().load(model.getAuthor().getAvatar())
                        .placeholder(R.drawable.ic_facb)
                        .into(avatar);
            } else {
                Picasso.get().load(R.drawable.ic_facb).into(avatar);
            }
            body.setText(model.getText());
            author.setText(model.getAuthor().getName());
            createdAt.setText(SimpleDateFormat.getDateInstance().format(model.getCreatedAt()));

            if (listener != null) {
                newComment.setOnClickListener(v -> listener.accept(model));
            }
        }
    }
}
