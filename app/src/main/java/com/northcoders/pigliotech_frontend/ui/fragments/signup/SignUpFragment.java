package com.northcoders.pigliotech_frontend.ui.fragments.signup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
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
import com.northcoders.pigliotech_frontend.ui.fragments.profile.ProfileFragment;

public class SignUpFragment extends Fragment {

    private EditText emailTextView, passwordTextView, nameTextView, avatarUrlTextView;
    private Button buttonConfirm;
    private ProgressBar progressbar;
    private FragmentSignUpBinding binding;
    private SignUpViewModel viewModel;
    private Spinner regionSpinner;
    private ArrayAdapter<CharSequence> arrayAdapter;

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

        bindingUiElements();

        buttonConfirm.setOnClickListener(view1 -> registerNewUser());

        NavigationBarView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(View.GONE);

        viewModel.getState().observe(requireActivity(), state -> {
            if (state.getLoading()){
                progressbar.setVisibility(View.VISIBLE);
            }else {
                progressbar.setVisibility(View.GONE);
            }
        });

        Context context = getContext();
        FragmentActivity activity = getActivity();

        /*
        events observer that uses the state of MutableLiveData<SignUpEvents> events in the
        SignUpFragment ViewModel
         */
        viewModel.getEvents().observe(requireActivity(), event -> {
            if(event != null){
                switch (event){
                    case REGISTRATION_SUCCESSFUL:
                        Toast.makeText(context,
                                        "Registration successful!",
                                        Toast.LENGTH_LONG)
                                .show();

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout_fragment, new ProfileFragment())
                                .commit();

                        NavigationBarView bottomNavBar = activity.findViewById(R.id.bottom_nav_bar);
                        bottomNavBar.setSelectedItemId(R.id.profile);

                        break;
                    case REGISTRATION_FAILED:
                        Toast.makeText(
                                        context,
                                        "Registration failed!!"
                                                + " Please try again later",
                                        Toast.LENGTH_LONG)
                                .show();
                        break;
                    case SELECT_REGION:
                        Toast.makeText(
                                        context,
                                        "Please select a Region!",
                                        Toast.LENGTH_LONG)
                                .show();
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
        region = regionSpinner.getSelectedItem().toString(); // TODO: To implement enum and spinner for Regions
        Log.i("REGION", region);

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        viewModel.signUp(name, email, password, avatarUrl, region);
    }

    private void bindingUiElements(){
        emailTextView = binding.email;
        passwordTextView = binding.password;
        nameTextView = binding.name;
        avatarUrlTextView = binding.url;
        buttonConfirm = binding.buttonConfirm;
        progressbar = binding.progressBar;

        // Setting up the spinner
        regionSpinner = binding.region;
        arrayAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.regions,
                R.layout.spinner_item
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionSpinner.setAdapter(arrayAdapter);
    }
}