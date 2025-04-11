package com.northcoders.pigliotech_frontend.presentation.features.signup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentSignUpBinding;
import com.northcoders.pigliotech_frontend.presentation.features.profile.ProfileFragment;

public class SignUpFragment extends Fragment {

    private EditText emailTextView, passwordTextView, nameTextView, avatarUrlTextView;
    private Button buttonConfirm;
    private ProgressBar progressbar;
    private FragmentSignUpBinding binding;
    private SignUpViewModel viewModel;
    private Spinner regionSpinner;
    private NavigationBarView bottomNavBar;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = requireActivity();
        Context context = requireContext();

        bindingUiElements();

        buttonConfirm.setOnClickListener(view1 -> registerNewUser());

        bottomNavBar.setVisibility(View.GONE);

        viewModel.getState().observe(activity, state -> {
            if (state.getLoading()){
                progressbar.setVisibility(View.VISIBLE);
            }else {
                progressbar.setVisibility(View.GONE);
            }
        });

        /*
        events observer that uses the state of MutableLiveData<SignUpEvents> events in the
        SignUpFragment ViewModel
         */
        viewModel.getEvents().observe(activity, event -> {
            if(event != null){
                switch (event){
                    case REGISTRATION_SUCCESSFUL:
                        Toast.makeText(
                                context,
                                "Registration successful!",
                                Toast.LENGTH_LONG
                        ).show();

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.frame_layout_fragment,
                                        new ProfileFragment()
                                ).commit();

                        activity.getSupportFragmentManager().popBackStack();

                        bottomNavBar.setSelectedItemId(R.id.profile);
                        break;
                    case REGISTRATION_FAILED, NETWORK_ERROR:
                        Toast.makeText(
                                context,
                                "Registration failed!! Please try again later",
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    case SELECT_REGION:
                        Toast.makeText(
                                context,
                                "Please select a Region!",
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    case NAME_IS_BLANK:
                        Toast.makeText(
                                context,
                                "Please enter your name!",
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    case EMAIL_IS_BLANK:
                        Toast.makeText(
                                context,
                                "Please enter email!",
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    case PASSWORD_IS_BLANK:
                        Toast.makeText(
                                context,
                                "Please enter password!",
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                    default:
                        break;
                }
                viewModel.eventSeen();
            }
        });
    }

    private void registerNewUser(){

        // Take the value of two edit texts in Strings
        String email, password, name, avatarUrl, region;

        name = nameTextView.getText().toString();
        Log.i("NAME", name);
        email = emailTextView.getText().toString();
        Log.i("EMAIL", email);
        password = passwordTextView.getText().toString();
        Log.i("PASSWORD", password);
        avatarUrl = avatarUrlTextView.getText().toString();
        Log.i("AVATARURL", avatarUrl);
        region = regionSpinner.getSelectedItem().toString();
        Log.i("REGION", region);


        viewModel.signUp(name, email, password, avatarUrl, region);
    }

    private void bindingUiElements(){
        emailTextView = binding.email;
        passwordTextView = binding.password;
        nameTextView = binding.name;
        avatarUrlTextView = binding.url;
        buttonConfirm = binding.buttonConfirm;
        progressbar = binding.progressBar;
        bottomNavBar = requireActivity().findViewById(R.id.bottom_nav_bar);

        // Setting up the spinner
        regionSpinner = binding.region;
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.regions,
                R.layout.spinner_item
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionSpinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}