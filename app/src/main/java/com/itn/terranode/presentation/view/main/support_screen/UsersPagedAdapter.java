package com.itn.terranode.presentation.view.main.support_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.itn.terranode.R;
import com.itn.terranode.data.network.dtos.NewsItem;
import com.itn.terranode.data.network.dtos.User;
import com.itn.terranode.presentation.view.main.news_screen.NewsPagedListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

class UsersPagedAdapter extends PagedListAdapter<User, UsersPagedAdapter.UsersViewHolder> {

    private OnItemClickListener listener;

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    };

    UsersPagedAdapter(UsersPagedAdapter.OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User item = getItem(position);
        if(item != null){
            holder.bind(item);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(User user);
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.constraintLayout)
        ConstraintLayout constraintLayout;
        @BindView(R.id.userNameTextView)
        TextView userNameTextView;
        @BindView(R.id.personalIdTextView)
        TextView personalIdTextView;
        @BindView(R.id.photoImageView)
        CircleImageView photoImageView;

        UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(User user) {
            userNameTextView.setText(user.getName());
            personalIdTextView.setText("ID: " + user.getId());
            if(user.getPhoto() == null){
                photoImageView.setImageResource(R.drawable.user);
            } else {
                Picasso.get()
                        .load(user.getPhoto())
                        .into(photoImageView);
            }

            constraintLayout.setOnClickListener(v -> listener.onItemClick(user));
        }
    }
}
