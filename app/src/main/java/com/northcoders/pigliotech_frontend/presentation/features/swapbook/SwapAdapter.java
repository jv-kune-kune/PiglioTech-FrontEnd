package com.northcoders.pigliotech_frontend.presentation.features.swapbook;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.northcoders.pigliotech_frontend.databinding.SwapItemBinding;
import com.northcoders.pigliotech_frontend.data.models.Book;
import com.northcoders.pigliotech_frontend.data.models.Match;
import com.northcoders.pigliotech_frontend.data.models.MatchUi;
import com.northcoders.pigliotech_frontend.data.models.User;

import java.util.List;

public class SwapAdapter extends RecyclerView.Adapter<SwapAdapter.SwapViewHolder> {

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

        // To Determine which information from the Match object is bound to the View
        if (viewModel.getUserId().equals(userOne.getUid())){
            MatchUi matchUi = new MatchUi(
                    userTwo.getName(),
                    userTwoBook.getTitle(),
                    userOneBook.getTitle()
            );
            holder.swapItemBinding.setMatchUi(matchUi);
        }else {
            MatchUi matchUi = new MatchUi(
                    userOne.getName(),
                    userOneBook.getTitle(),
                    userTwoBook.getTitle()
            );
            holder.swapItemBinding.setMatchUi(matchUi);
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