package com.northcoders.pigliotech_frontend.ui.fragments.errorpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentErrorBinding;
import com.northcoders.pigliotech_frontend.ui.fragments.landingpage.LandingPageFragment;

public class ErrorFragment extends Fragment {

    private FragmentErrorBinding binding;

    public ErrorFragment() {
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
        binding = FragmentErrorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button tryAgainBtn = binding.tryAgainBtn;
        NavigationBarView bottomNavBar = requireActivity().findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setVisibility(View.GONE);

        tryAgainBtn.setOnClickListener(
                view1 -> {
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.frame_layout_fragment,
                                    new LandingPageFragment()
                            ).commit();

                    requireActivity().getSupportFragmentManager().popBackStack();
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}