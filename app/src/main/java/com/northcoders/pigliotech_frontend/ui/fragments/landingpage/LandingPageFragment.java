package com.northcoders.pigliotech_frontend.ui.fragments.landingpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.navigation.NavigationBarView;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentLandingPageBinding;
import com.northcoders.pigliotech_frontend.ui.fragments.login.LoginFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.signup.SignUpFragment;


public class LandingPageFragment extends Fragment {

    private Button btnLogin, btnSignUp;
    private FragmentLandingPageBinding binding;

    public LandingPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLandingPageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogin = binding.btnLogin;
        btnSignUp = binding.btnSignUp;

        LoginFragment loginFragment = new LoginFragment();
        SignUpFragment signUpFragment = new SignUpFragment();

        btnLogin.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_fragment, loginFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnSignUp.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_fragment, signUpFragment)
                    .addToBackStack(null)
                    .commit();
        });

        NavigationBarView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(View.GONE);
    }
}