package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.northcoders.pigliotech_frontend.databinding.SwapItemBinding;
import com.northcoders.pigliotech_frontend.model.Match;

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

        Match match = matches.get(position); // TODO
        // TODO implement data binding in the xml
        // holder.swapItemBinding.setSwap(swap)


        holder.declineButton.setOnClickListener(view -> {
            // TODO
            viewModel.declineButtonClicked();
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class SwapViewHolder extends RecyclerView.ViewHolder{
        private final SwapItemBinding swapItemBinding;
        private final Button declineButton;


        public SwapViewHolder(SwapItemBinding swapItemBinding) {
            super(swapItemBinding.getRoot());
            this.swapItemBinding = swapItemBinding;
            this.declineButton = swapItemBinding.declineBtn;
        }
    }
}