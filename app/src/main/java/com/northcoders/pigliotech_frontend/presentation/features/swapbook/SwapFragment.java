package com.northcoders.pigliotech_frontend.presentation.features.swapbook;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
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
import android.widget.Toast;

import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentSwapBinding;
import com.northcoders.pigliotech_frontend.data.models.Match;
import com.northcoders.pigliotech_frontend.presentation.features.error.ErrorFragment;

import java.util.List;

public class SwapFragment extends Fragment {

    private FragmentSwapBinding binding;
    private SwapViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Match> matches;

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
        Context context = requireContext();

        viewModel.getState().observe(getViewLifecycleOwner(), swapState -> {
            if (swapState instanceof SwapState.Loading) {
                progressBar.setVisibility(VISIBLE);
            } else if (swapState instanceof SwapState.Loaded) {
                progressBar.setVisibility(GONE);
                matches = ((SwapState.Loaded) swapState).matches();
                displayInRecyclerView();
            } else if (swapState instanceof SwapState.Error) {
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_fragment, new ErrorFragment())
                        .commit();

                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });

        // Events observer
        viewModel.getEvents().observe(getViewLifecycleOwner(), swapEvents -> {
            if (swapEvents != null) {
                switch (swapEvents) {
                    case DISMISS_MATCH ->
                        Toast.makeText(
                                context,
                                "Swap Request Dismissed",
                                Toast.LENGTH_LONG)
                                .show();

                    case DISMISS_MATCH_FAILED ->
                        Toast.makeText(
                                context,
                                "Swap Request Dismissal Failed!",
                                Toast.LENGTH_LONG)
                                .show();

                    case NETWORK_ERROR ->
                        Toast.makeText(
                                context,
                                "Sorry Something Went Wrong!",
                                Toast.LENGTH_LONG)
                                .show();
                }
                viewModel.eventSeen();
            }
        });
    }

    public void displayInRecyclerView() {
        SwapAdapter swapAdapter = new SwapAdapter(matches, viewModel);
        recyclerView.setAdapter(swapAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}