package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.northcoders.pigliotech_frontend.databinding.SwapItemBinding;

import java.util.List;

public class SwapAdapter extends RecyclerView.Adapter<SwapAdapter.SwapViewHolder> {

    // TODO: To implement
    private final List<Object> swaps;

    public SwapAdapter(List<Object> swaps) {
        this.swaps = swaps;
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

        Object swap = swaps.get(position); // TODO
        // TODO implement data binding in the xml
        // holder.swapItemBinding.setSwap(swap)
    }

    @Override
    public int getItemCount() {
        return swaps.size();
    }

    public static class SwapViewHolder extends RecyclerView.ViewHolder{
        private final SwapItemBinding swapItemBinding;

        public SwapViewHolder(SwapItemBinding swapItemBinding) {
            super(swapItemBinding.getRoot());
            this.swapItemBinding = swapItemBinding;
        }
    }
}