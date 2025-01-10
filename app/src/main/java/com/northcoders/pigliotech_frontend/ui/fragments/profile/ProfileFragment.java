package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentProfileBinding;
import com.northcoders.pigliotech_frontend.ui.fragments.landingpage.LandingPageFragment;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView textViewEmail, textViewName, textViewRegion;
    private FragmentProfileBinding binding;
    private Button btnSignOut;
    private ProfileViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();


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

        FirebaseUser currentUser = mAuth.getCurrentUser();
        LandingPageFragment landingPageFragment = new LandingPageFragment();

        textViewEmail = binding.email;
        textViewName = binding.name;
        textViewRegion = binding.region;

        btnSignOut = binding.buttonSignOut;

        if (currentUser != null ){
            textViewName.setText(currentUser.getDisplayName());
            textViewEmail.setText(currentUser.getEmail());
        }

        btnSignOut.setOnClickListener(view1 ->{
            mAuth.signOut();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_fragment, landingPageFragment)
                    .commit();
        });

        NavigationBarView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(View.VISIBLE);

    }
}