package com.northcoders.pigliotech_frontend.ui.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationBarView;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentLoginBinding;
import com.northcoders.pigliotech_frontend.ui.fragments.home.HomeFragment;

public class LoginFragment extends Fragment {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create ViewModel Instance
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
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

        FragmentActivity activity = requireActivity();
        Context context = requireContext();

        bindUiElements();

        btnLogin.setOnClickListener(view1 -> loginUserAccount());

        NavigationBarView bottomNav = activity.findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(View.GONE);

        viewModel.getState().observe(activity, loginState -> {
            if (loginState.getLoading()) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        /*
        events observer that uses the state of MutableLiveData<Login> events in the
        SignUpFragment ViewModel
         */
        viewModel.getEvents().observe(activity, loginEvent -> {
            if (loginEvent != null) {
                switch (loginEvent) {
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

                        activity.getSupportFragmentManager().popBackStack();

                        // Sets the selected item in the BottomNavBar to the Home Icon.
                        bottomNav.setSelectedItemId(R.id.home);
                        break;
                    case LOGIN_FAILED:
                        Toast.makeText(
                                context,
                                "Login failed!!",
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
                }
                viewModel.eventSeen(); // Set the loginEvent back to null
            }
        });
    }

    private void loginUserAccount() {
        // Take the value of two edit texts in Strings
        String email, password;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        viewModel.login(email, password);
    }

    private void bindUiElements() {
        editTextEmail = binding.email;
        editTextPassword = binding.password;
        progressBar = binding.progressBar;
        btnLogin = binding.buttonLogin;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}