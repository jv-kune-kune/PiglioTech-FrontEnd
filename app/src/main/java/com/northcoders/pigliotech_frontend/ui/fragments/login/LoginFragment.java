package com.northcoders.pigliotech_frontend.ui.fragments.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentLoginBinding;
import com.northcoders.pigliotech_frontend.ui.fragments.home.HomeFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.profile.ProfileFragment;
import com.northcoders.pigliotech_frontend.ui.fragments.signup.SignUpViewModel;


public class LoginFragment extends Fragment {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    //    private final ProfileFragment profileFragment = new ProfileFragment();
    private LoginViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance(); //FirebaseAuth Instance
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create ViewModel Instance
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        editTextEmail = binding.email;
        editTextPassword = binding.password;
        progressBar = binding.progressBar;
        btnLogin = binding.buttonLogin;

        btnLogin.setOnClickListener(view1 -> loginUserAccount());

        NavigationBarView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(View.GONE);

        viewModel.getState().observe(requireActivity(), loginState -> {
            if(loginState.getLoading()){
                progressBar.setVisibility(View.VISIBLE);
            }else {
                progressBar.setVisibility(View.GONE);
            }
        });

        Context context = getContext();
        FragmentActivity activity = getActivity();

        /*
        events observer that uses the state of MutableLiveData<Login> events in the
        SignUpFragment ViewModel
         */
        viewModel.getEvents().observe(requireActivity(), loginEvent -> {
            if(loginEvent != null){
                switch(loginEvent){
                    case LOGIN_SUCCESSFUL:
                        // On successful login, create a toast and navigate to home fragment
                        Toast.makeText(
                                context,
                                "Login successful!!",
                                Toast.LENGTH_LONG
                        ).show();

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.frame_layout_fragment,
                                        new HomeFragment()
                                ).commit();

                        break;
                    case LOGIN_FAILED:
                        Toast.makeText(
                                context,
                                "Login failed!!",
                                Toast.LENGTH_LONG
                        ).show();
                        break;
                }
                viewModel.eventSeen(); // Set the loginEvent back to null
            }
        });
    }

    private void loginUserAccount(){

        // show the visibility of progress bar to show loading
//        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        // validations for input email and password
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

        viewModel.login(email, password);

        // signin existing user
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(
//                        new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(
//                                    @NonNull Task<AuthResult> task)
//                            {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(getContext(),
//                                                    "Login successful!!",
//                                                    Toast.LENGTH_LONG)
//                                            .show();
//
//                                    // hide the progress bar
//                                    progressBar.setVisibility(View.GONE);
//
//                                    // if sign-in is successful
//                                    // intent to home activity
//                                    if (getActivity() != null){
//                                        getActivity().getSupportFragmentManager()
//                                                .beginTransaction()
//                                                .replace(
//                                                        R.id.frame_layout_fragment,
//                                                        profileFragment
//                                                ).commit();
//                                    }
//                                }
//
//                                else {
//
//                                    // sign-in failed
//                                    Toast.makeText(getContext(),
//                                                    "Login failed!!",
//                                                    Toast.LENGTH_LONG)
//                                            .show();
//
//                                    // hide the progress bar
//                                    progressBar.setVisibility(View.GONE);
//                                }
//                            }
//                        }
//                );
    }

}