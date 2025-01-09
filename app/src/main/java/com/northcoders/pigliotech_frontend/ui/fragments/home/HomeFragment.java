package com.northcoders.pigliotech_frontend.ui.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationBarView;
import com.northcoders.pigliotech_frontend.R;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NavigationBarView bottomNav = getActivity().findViewById(R.id.bottom_nav_bar);
        bottomNav.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}