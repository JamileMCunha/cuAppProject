package com.cuApp.features.dashboard.feed;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cuApp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentItem> {

    private Context context;
    private List<CommentModel> comments = new ArrayList<>();

    CommentsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CommentItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentItem holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    void push(CommentModel commentModel) {
        comments.add(commentModel);
        notifyItemInserted(comments.size());
    }

    void update(CommentModel commentModel) {
        int position = findPosition(commentModel);
        if (position > -1) {
            comments.set(position, commentModel);
            notifyItemChanged(position);
            return;
        }
        push(commentModel);
    }

    void remove(CommentModel commentModel) {
        int position = findPosition(commentModel);
        if (position > -1) {
            comments.remove(position);
            notifyItemRemoved(position);
        }
    }

    private int findPosition(CommentModel commentModel) {
        int i = -1;
        for (CommentModel model : comments) {
            i++;
            if (model.equals(commentModel)) {
                return i;
            }
        }
        return i;
    }

    static class CommentItem extends RecyclerView.ViewHolder {

        private ImageView avatar;
        private TextView body;
        private TextView author;
        private TextView createdAt;

        CommentItem(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            body = itemView.findViewById(R.id.body);
            author = itemView.findViewById(R.id.author);
            createdAt = itemView.findViewById(R.id.createdAt);
        }

        void bind(CommentModel model) {
            if (model.getAuthor() != null && !TextUtils.isEmpty(model.getAuthor().getAvatar())) {
                Picasso.get().load(model.getAuthor().getAvatar()).placeholder(R.drawable.ic_facb).into(avatar);
            } else {
                Picasso.get().load(R.drawable.ic_facb).into(avatar);
            }
            body.setText(model.getText());
            author.setText(model.getAuthor().getName());
            createdAt.setText(SimpleDateFormat.getDateInstance().format(model.getCreatedAt()));
        }
    }
}
