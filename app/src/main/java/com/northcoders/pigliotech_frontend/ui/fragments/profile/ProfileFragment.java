package com.northcoders.pigliotech_frontend.ui.fragments.profile;

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
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<Book> userBooks;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        viewModel.load();

        if(getArguments() != null) {
            Log.i("Profile fragment", "passed user id: " + getArguments().getString("userId"));
        } else {
            Log.i("Profile fragment", "no user id");
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

        textViewEmail = binding.email;
        textViewName = binding.name;
        textViewRegion = binding.region;
        ProgressBar progressBar = binding.progressBar;

        btnSignOut = binding.buttonSignOut;

        // Observe the state from the viewModel
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {

            if (state instanceof ProfileState.Loading){
                progressBar.setVisibility(View.VISIBLE);
            }else if (state instanceof ProfileState.Loaded){
                progressBar.setVisibility(View.GONE);
                textViewName.setText(((ProfileState.Loaded) state).getName());
                textViewEmail.setText(((ProfileState.Loaded) state).getEmail());
                textViewRegion.setText(((ProfileState.Loaded) state).getRegion());
                userBooks = (ArrayList<Book>) ((ProfileState.Loaded) state).getBooks();
                displayUserRecyclerView(); // Initialise the RecyclerView when the userBooks has data
                Log.i("ProfileFragment: Books", userBooks.toString());
            }
        });

        btnSignOut.setOnClickListener(view1 ->{

            viewModel.signOut();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_fragment, landingPageFragment)
                    .commit();
        });

        // Instantiate the BottomNavigationBarView and set it to Visible
        NavigationBarView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(View.VISIBLE);
    }

    private void displayUserRecyclerView(){
        //RecyclerView Set Up
        recyclerView = binding.bookListRecyclerView;
        userAdapter = new UserAdapter(userBooks);
        recyclerView.setAdapter(userAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}