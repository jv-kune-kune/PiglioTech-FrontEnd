package com.northcoders.pigliotech_frontend.presentation.features.addbook;

import static android.view.View.*;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.databinding.FragmentAddBookBinding;
import com.northcoders.pigliotech_frontend.presentation.features.profile.ProfileFragment;

public class AddBookFragment extends Fragment {

    private AddBookViewModel viewModel;
    private EditText editTextIsbn;
    private ProgressBar progressBar;
    private Button buttonSubmit;
    private Button buttonScan;
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

        uiElementBinding(); // Bind UI Elements

        buttonSubmit.setOnClickListener(
                view1 -> viewModel.addBook(editTextIsbn.getText().toString())
        );

        buttonScan.setOnClickListener(
                view1 -> {
                    if (getContext() != null){
                        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(getContext());
                        scanner.startScan()
                                .addOnSuccessListener(barcode -> {
                                    if(barcode.getRawValue()!= null){
                                        editTextIsbn.setText(barcode.getRawValue());
                                        Log.i("AddBookFragment", barcode.getRawValue());
                                    }

                                });
                    }
                }
        );

        // State observer
        viewModel.getState().observe(getViewLifecycleOwner(), addBookState -> {
            if (addBookState.getLoading()){
                progressBar.setVisibility(VISIBLE);
            }else {
                progressBar.setVisibility(GONE);
            }
        });

        // For toasts and supportFragmentManager in the Events Observer
        Context context = getContext();
        FragmentActivity activity = getActivity();

        // Events observer
        viewModel.getEvents().observe(getViewLifecycleOwner(), event ->{
            if (event != null && activity != null){
                switch (event){
                    case BOOK_ADDED:
                        Toast.makeText(context,
                                        "Book Added!",
                                        Toast.LENGTH_LONG)
                                .show();

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.frame_layout_fragment,
                                        new ProfileFragment())
                                .commit();
                        break;
                    case BOOK_NOT_ADDED:
                        Toast.makeText(context,
                                        "Book Was Not Added!",
                                        Toast.LENGTH_LONG)
                                .show();
                        break;
                    case BOOK_ALREADY_OWNED:
                        Toast.makeText(context,
                                        "This Book Is Already In Your Library!",
                                        Toast.LENGTH_LONG)
                                .show();
                        break;
                    case INVALID_ISBN:
                        Toast.makeText(context,
                                        "Please Enter a valid ISBN!",
                                        Toast.LENGTH_LONG)
                                .show();
                        break;
                    case NETWORK_ERROR:
                        Toast.makeText(context,
                                        "Network Error Please Try Again Later!",
                                        Toast.LENGTH_LONG)
                                .show();
                        break;
                    default:
                        break;
                }
                viewModel.eventSeen(); // sets the event back to null
            }
        });
    }

    private void uiElementBinding(){
        editTextIsbn = binding.isbn;
        progressBar = binding.progressBar;
        buttonSubmit = binding.buttonSubmit;
        buttonScan = binding.buttonScanBook;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}