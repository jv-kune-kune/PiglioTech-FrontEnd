package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.northcoders.pigliotech_frontend.databinding.SwapItemBinding;
import com.northcoders.pigliotech_frontend.model.Book;
import com.northcoders.pigliotech_frontend.model.Match;
import com.northcoders.pigliotech_frontend.model.User;

import java.util.List;

public class SwapAdapter extends RecyclerView.Adapter<SwapAdapter.SwapViewHolder> {

    // TODO: To implement
    private final List<Match> matches;
    private final SwapViewModel viewModel;

    public SwapAdapter(List<Match> matches, SwapViewModel viewModel) {
        this.matches = matches;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SwapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SwapItemBinding binding = SwapItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new SwapViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SwapViewHolder holder, int position) {

        Match match = matches.get(position);
        User userOne = match.userOne();
        User userTwo = match.userTwo();
        Book userOneBook = match.userOneBook();
        Book userTwoBook = match.userTwoBook();

        Log.i("SwapAdapter", "Current Match: " + match);

        // TODO SET UP MODEL

        if (viewModel.getUserId().equals(userOne.getUid())){
            holder.requesterNameTextView.setText(userTwo.getName());
            holder.requestBookTitleTextView.setText(userTwoBook.getTitle());
            holder.userBookTitleTextView.setText(userOneBook.getTitle());
        }else {
            holder.requesterNameTextView.setText(userOne.getName());
            holder.requestBookTitleTextView.setText(userOneBook.getTitle());
            holder.userBookTitleTextView.setText(userTwoBook.getTitle());
        }

        holder.declineButton.setOnClickListener(
                view -> viewModel.declineButtonClicked(match.id())
        );
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class SwapViewHolder extends RecyclerView.ViewHolder{
        private final SwapItemBinding swapItemBinding;
        private final Button declineButton;
        private final TextView requesterNameTextView;
        private final TextView requestBookTitleTextView;
        private final TextView userBookTitleTextView;


        public SwapViewHolder(SwapItemBinding swapItemBinding) {
            super(swapItemBinding.getRoot());
            this.swapItemBinding = swapItemBinding;
            this.declineButton = swapItemBinding.declineBtn;
            this.requesterNameTextView = swapItemBinding.requesterNameTextView;
            this.requestBookTitleTextView = swapItemBinding.otherUserTitle;
            this.userBookTitleTextView = swapItemBinding.userTitle;
        }
    }
}