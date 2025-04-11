package com.northcoders.pigliotech_frontend.ui.fragments.home;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentHomeBinding;
import com.northcoders.pigliotech_frontend.model.User;
import com.northcoders.pigliotech_frontend.ui.fragments.errorpage.ErrorFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.profile.ProfileFragment;

import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private RecyclerView recyclerView;
    private List<User> users;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        viewModel.load();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavigationBarView bottomNav = requireActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(VISIBLE);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar progressBar;
        recyclerView = binding.libRecyclerView;
        progressBar = binding.progressBar;

        // Attach an empty adapter initially to prevent warning
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new LibraryAdapter(Collections.emptyList(), viewModel));
        recyclerView.setHasFixedSize(true);

        viewModel.getState().observe(getViewLifecycleOwner(), homeState -> {
            if (homeState instanceof HomeState.Loading) {
                progressBar.setVisibility(VISIBLE);
            } else if (homeState instanceof HomeState.Loaded loaded) {
                progressBar.setVisibility(GONE);
                users = loaded.otherUserLibraries();
                displayInRecyclerView();
            } else if (homeState instanceof HomeState.Error) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_fragment, new ErrorFragment())
                        .commit();
                requireActivity().getSupportFragmentManager().popBackStack();
                viewModel.signOut();
            }
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), homeEvents -> {
            if (((HomeEvents.ClickedUserLibrary) homeEvents).clickedUserId() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", ((HomeEvents.ClickedUserLibrary) homeEvents).clickedUserId());

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_fragment, profileFragment)
                        .addToBackStack(null)
                        .commit();

                viewModel.eventSeen();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void displayInRecyclerView() {
        LibraryAdapter libraryAdapter = new LibraryAdapter(users, viewModel);
        recyclerView.setAdapter(libraryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        libraryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
