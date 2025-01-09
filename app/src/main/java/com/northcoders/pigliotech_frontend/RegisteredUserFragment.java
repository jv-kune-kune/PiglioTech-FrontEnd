package com.northcoders.pigliotech_frontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.northcoders.pigliotech_frontend.databinding.FragmentRegisteredUserBinding;


public class RegisteredUserFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView textViewEmail, textViewUuid;
    private FragmentRegisteredUserBinding binding;
    private Button btnSignOut;

    public RegisteredUserFragment() {
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

        binding = FragmentRegisteredUserBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        LandingPageFragment landingPageFragment = new LandingPageFragment();

        if (currentUser != null){
            textViewEmail = binding.textviewEmail;
            textViewUuid = binding.textviewUuid;

            textViewEmail.setText(currentUser.getEmail());

            if (currentUser.getDisplayName() != null){
                Log.i("Display Name", currentUser.getDisplayName());
            }

            textViewUuid.setText(currentUser.getUid());
        }

        btnSignOut = binding.btnSignOut;

        btnSignOut.setOnClickListener(view1 ->{
            mAuth.signOut();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_fragment, landingPageFragment)
                    .commit();
        });

    }
}