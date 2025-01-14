package com.northcoders.pigliotech_frontend.ui.fragments.home;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.LibraryViewBinding;
import com.northcoders.pigliotech_frontend.model.User;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {

    List<User> users;
    Context context;
    HomeViewModel viewmodel;

    public LibraryAdapter(List<User> users, Context context, HomeViewModel viewmodel) {
        this.users = users;
        this.context = context;
        this.viewmodel = viewmodel;
    }

    public static class LibraryViewHolder extends RecyclerView.ViewHolder {
        private LibraryViewBinding libraryItemBinding;

        public LibraryViewHolder(LibraryViewBinding libraryItemBinding) {
            super(libraryItemBinding.getRoot());
            this.libraryItemBinding = libraryItemBinding;

        }
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LibraryViewBinding binding;
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.library_view, parent, false);

        return new LibraryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {

        User user = users.get(position);
        String url = user.getThumbnail();

        Glide.with(holder.itemView.getContext())
                        .load(url)
                        .placeholder(R.drawable.blank_pfp)
                        .into(holder.libraryItemBinding.pfpImage);

        holder.libraryItemBinding.setUser(user);

        holder.libraryItemBinding.libraryCard.setOnClickListener(view -> {
            viewmodel.onUserClicked(user.getUid());
            Log.i("Library Adapter", "RecyclerView item clicked" );
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

}
