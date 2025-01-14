package com.northcoders.pigliotech_frontend.ui.fragments.home;

import static android.view.View.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationBarView;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentHomeBinding;
import com.northcoders.pigliotech_frontend.model.User;
import com.northcoders.pigliotech_frontend.model.service.FirebaseInstance;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

import java.util.List;
import java.util.function.Consumer;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<User> users;
    private  LibraryAdapter libraryAdapter;

    // TODO clickability

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    public void displayInRecyclerView() {
        recyclerView = binding.libRecyclerView;
        libraryAdapter = new LibraryAdapter(users, this.getContext());

        recyclerView.setAdapter(libraryAdapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this.getContext())
        );

        recyclerView.setHasFixedSize(true);

        libraryAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NavigationBarView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(VISIBLE);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.libRecyclerView;
        progressBar = binding.progressBar;

        viewModel.getState().observe(getViewLifecycleOwner(), homeState -> {
            if (homeState instanceof HomeState.Loading){
                progressBar.setVisibility(VISIBLE);
            }else if (homeState instanceof  HomeState.Loaded){
                progressBar.setVisibility(GONE);
                users = ((HomeState.Loaded) homeState).getOtherUserLibraries();
                displayInRecyclerView();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}