package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import android.content.Context;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentProfileBinding;
import com.northcoders.pigliotech_frontend.model.Book;
import com.northcoders.pigliotech_frontend.ui.fragments.landingpage.LandingPageFragment;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private TextView textViewEmail, textViewName, textViewRegion;
    private FragmentProfileBinding binding;
    private Button btnSignOut;
    private ProfileViewModel viewModel;
    private ArrayList<Book> userBooks;
    private ProgressBar progressBar;
    private final String TAG = "ProfileFragment";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        // If getArguments is not null it will pass the User id from Home as a param.
        if(getArguments() != null) {
            viewModel.load(getArguments().getString("userId"));
            Log.i(TAG, "passed user id: " + getArguments().getString("userId"));
        } else {
            viewModel.load(null);
            Log.i(TAG, "no user id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LandingPageFragment landingPageFragment = new LandingPageFragment();

        bindingUiElements();

        Context context = getContext();

        // Observe the state from the viewModel
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {

            if (state instanceof ProfileState.Loading){
                progressBar.setVisibility(View.VISIBLE);
            }else if (state instanceof ProfileState.Loaded){

                setUpCurrentUserScreen((ProfileState.Loaded) state);

            } else if (state instanceof ProfileState.OtherUserLoaded) {

                setUpNonCurrentUserScreen((ProfileState.OtherUserLoaded) state);
            }
        });

        // The Events observer for the ProfileFragment
        viewModel.getEvents().observe(getViewLifecycleOwner(), profileEvents -> {
            if (profileEvents != null){
                switch (profileEvents) {
                    case BOOK_DELETED ->
                            Toast.makeText(
                                            context,
                                            "Book Deleted",
                                            Toast.LENGTH_LONG)
                                    .show();
                    case BOOK_NOT_DELETED -> Toast.makeText(
                                    context,
                                    "Book Not Deleted!",
                                    Toast.LENGTH_LONG)
                            .show();
                    case BOOK_LIKED ->
                            Toast.makeText(
                                            requireContext(),
                                            "Book Liked",
                                            Toast.LENGTH_LONG)
                                    .show();
                    case BOOK_ALREADY_LIKED ->
                            Toast.makeText(
                                            requireContext(),
                                            "Book Already Liked",
                                            Toast.LENGTH_LONG)
                                    .show();

                    case LIKE_ERROR ->
                            Toast.makeText(
                                            requireContext(),
                                            "Sorry, Could Not Like Book!",
                                            Toast.LENGTH_LONG)
                                    .show();
                }
                viewModel.eventSeen();
            }
        });

        btnSignOut.setOnClickListener(view1 ->{

            viewModel.signOut();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_fragment, landingPageFragment)
                    .commit();
        });

        // Instantiate the BottomNavigationBarView and set it to Visible
        NavigationBarView bottomNav = requireActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(View.VISIBLE);
    }

    private void bindingUiElements(){
        textViewEmail = binding.email;
        textViewName = binding.name;
        textViewRegion = binding.region;
        progressBar = binding.progressBar;
        btnSignOut = binding.buttonSignOut;
    }

    private void displayUserRecyclerView(){
        //RecyclerView Set Up
        RecyclerView recyclerView = binding.bookListRecyclerView;
        UserAdapter userAdapter = new UserAdapter(userBooks, viewModel, viewModel.getState().getValue());
        recyclerView.setAdapter(userAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        userAdapter.notifyDataSetChanged();
    }

    // Setup the ProfileScreen View for the current user
    private void setUpCurrentUserScreen(ProfileState.Loaded state){
        progressBar.setVisibility(View.GONE);
        textViewName.setText(state.name());
        textViewEmail.setText(state.email());
        textViewRegion.setText(state.region());
        userBooks = (ArrayList<Book>) state.books();
        displayUserRecyclerView(); // Initialise the RecyclerView when the userBooks has data
        Log.i(TAG,"Books: "+ userBooks.toString());
    }

    // Setup the ProfileScreen View for selected Non-current user Library
    private void setUpNonCurrentUserScreen(ProfileState.OtherUserLoaded state){
        progressBar.setVisibility(View.GONE);
        textViewName.setText(state.name());
        textViewEmail.setVisibility(View.GONE);
        textViewRegion.setVisibility(View.GONE);
        btnSignOut.setVisibility(View.GONE);
        userBooks = (ArrayList<Book>) state.books();
        displayUserRecyclerView(); // Initialise the RecyclerView when the userBooks has data
        Log.i(TAG,"Books: "+ userBooks.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}