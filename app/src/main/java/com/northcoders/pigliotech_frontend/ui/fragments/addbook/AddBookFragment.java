package com.northcoders.pigliotech_frontend.ui.fragments.addbook;

import static android.view.View.*;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.northcoders.pigliotech_frontend.databinding.FragmentAddBookBinding;

public class AddBookFragment extends Fragment {

    private AddBookViewModel viewModel;
    private EditText editTextIsbn;
    private ProgressBar progressBar;
    private Button buttonSubmit;
    private FragmentAddBookBinding binding;

    public AddBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(AddBookViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBookBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextIsbn = binding.isbn;
        progressBar = binding.progressBar;
        buttonSubmit = binding.buttonSubmit;


        // State observer
        viewModel.getState().observe(getViewLifecycleOwner(), addBookState -> {
            if (addBookState.getLoading()){
                progressBar.setVisibility(VISIBLE);
            }else {
                progressBar.setVisibility(GONE);
            }
        });

        // For toasts and supportFragmentManager
        Context context = getContext();
        FragmentActivity activity = getActivity();

        // Events observer
        viewModel.getEvents().observe(getViewLifecycleOwner(), event ->{
            if (event != null){
                switch (event){
                    case BOOK_ADDED:
                        // TODO
                        break;
                    case BOOK_NOT_ADDED:
                        // TODO
                        break;
                    case INVALID_ISBN:
                        // TODO
                        break;
                    default:
                        break;
                }
                viewModel.eventSeen(); // sets the event back to null
            }
        });
    }
}