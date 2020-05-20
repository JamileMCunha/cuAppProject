package com.cuApp.features.dashboard.chat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.cuApp.R;
import com.cuApp.core.Constants;
import com.cuApp.features.auth.AuthContext;
import com.cuApp.features.auth.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ItemHolder> {

    private Context context;
    private List<User> users = new ArrayList<>();
    private Consumer<User> clickListener;

    ListUserAdapter(Context context) {
        this.context = context;
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User me = AuthContext.getUser();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User value = child.getValue(User.class);
                    if (value == null || value.getUid().equals(me.getUid())) continue;
                    users.add(value);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(Constants.TAG, "Can't listen", databaseError.toException());
            }
        });
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bind(users.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    void setOnItemClickListener(Consumer<User> clickListener) {
        this.clickListener = clickListener;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ImageView avatar;
        private TextView name;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.avatar = itemView.findViewById(R.id.avatar);
            this.name = itemView.findViewById(R.id.name);
        }

        void bind(User user, Consumer<User> consumer) {
            if (!TextUtils.isEmpty(user.getImage())) {
                Picasso.get().load(user.getImage()).placeholder(R.drawable.ic_facb).into(avatar);
            } else {
                Picasso.get().load(R.drawable.ic_facb).into(avatar);
            }
            name.setText(user.getName());
            itemView.setOnClickListener(v -> {
                if (consumer != null) {
                    consumer.accept(user);
                }
            });
        }
    }
}
