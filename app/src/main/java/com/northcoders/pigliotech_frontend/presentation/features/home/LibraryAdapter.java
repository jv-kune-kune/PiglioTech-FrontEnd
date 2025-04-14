package com.northcoders.pigliotech_frontend.presentation.features.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.LibraryViewBinding;
import com.northcoders.pigliotech_frontend.data.models.User;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {

    private final List<User> users;
    private final HomeViewModel viewmodel;

    public LibraryAdapter(List<User> users, HomeViewModel viewmodel) {
        this.users = users;
        this.viewmodel = viewmodel;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LibraryViewBinding binding;
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.library_view, parent,
                false);

        return new LibraryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {

        User user = users.get(position);

        // Add null checks for thumbnail
        String url = user.getThumbnail();

        Glide.with(holder.pfpImageView.getContext())
                .load(url)
                .placeholder(R.drawable.blank_pfp)
                .into(holder.pfpImageView);

        // Set the user object to the binding, the view binding now has null checks
        holder.libraryItemBinding.setUser(user);

        holder.libraryItemBinding.libraryCard.setOnClickListener(view -> {
            if (user.getUid() != null) {
                viewmodel.onUserClicked(user.getUid());
                Log.i("LibraryAdapter", "RecyclerView item clicked");
            } else {
                Log.e("LibraryAdapter", "User UID is null");
            }
        });
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    public static class LibraryViewHolder extends RecyclerView.ViewHolder {
        private final LibraryViewBinding libraryItemBinding;
        private final ImageView pfpImageView;

        public LibraryViewHolder(LibraryViewBinding libraryItemBinding) {
            super(libraryItemBinding.getRoot());
            this.libraryItemBinding = libraryItemBinding;
            this.pfpImageView = libraryItemBinding.pfpImage;
        }
    }
}