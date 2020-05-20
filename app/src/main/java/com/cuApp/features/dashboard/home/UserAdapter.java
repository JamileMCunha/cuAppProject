package com.cuApp.features.dashboard.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.cuApp.R;
import com.cuApp.features.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserItem> {

    private Context context;
    private List<User> users = new ArrayList<>();

    UserAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public UserItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_rows, viewGroup, false);
        return new UserItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItem holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    static class UserItem extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name, college, course;

        UserItem(View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatarp);
            name = itemView.findViewById(R.id.namep);
            college = itemView.findViewById(R.id.collegep);
            course = itemView.findViewById(R.id.coursep);

        }

        void bind(User user) {
            if (!TextUtils.isEmpty(user.getImage())) {
                Picasso.get().load(user.getImage())
                        .placeholder(R.drawable.ic_facb)
                        .into(avatar);
            } else {
                Picasso.get().load(R.drawable.ic_facb).into(avatar);
            }
            name.setText(user.getName());
            college.setText(user.getCollege());
            course.setText(user.getCourse());
        }
    }

}
