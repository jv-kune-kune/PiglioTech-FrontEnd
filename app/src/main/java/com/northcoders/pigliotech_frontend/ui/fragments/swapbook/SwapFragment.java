package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.northcoders.pigliotech_frontend.databinding.FragmentSwapBinding;

import java.util.List;

public class SwapFragment extends Fragment {

    private FragmentSwapBinding binding;
    private SwapViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Object> swaps;

    public SwapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SwapViewModel.class);
        viewModel.load();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSwapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.swapRecyclerView;
        progressBar = binding.progressBar;

        viewModel.getState().observe(getViewLifecycleOwner(), swapState -> {
            if (swapState instanceof  SwapState.Loading){
                progressBar.setVisibility(VISIBLE);
            } else if (swapState instanceof SwapState.Loaded){
                progressBar.setVisibility(GONE);
                swaps = ((SwapState.Loaded) swapState).swaps();
                displayInRecyclerView();
            }
        });

        // Events observer
        viewModel.getEvents().observe(getViewLifecycleOwner(), swapEvents -> {
            if (swapEvents != null){
                switch (swapEvents){
                    case ACCEPT_SWAP -> {
                        // TODO
                    }
                    case DENY_SWAP -> {
                        // TODO
                    }
                }
                viewModel.eventSeen();
            }
        });
    }

    public void displayInRecyclerView(){

        SwapAdapter swapAdapter = new SwapAdapter(swaps, viewModel);
        recyclerView.setAdapter(swapAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        swapAdapter.notifyDataSetChanged();
    }
}