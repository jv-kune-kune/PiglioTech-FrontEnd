package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;

import com.northcoders.pigliotech_frontend.model.Book;

import java.util.List;
import java.util.Objects;

// The states that the ProfileFragment can be in
public interface SwapState {

    record Loading() implements SwapState {}

    record Loaded(
            List<Objects> swaps
            // TODO update list type with Swap object
    ) implements SwapState {}


    // TODO LOOK INTO HANDLING ERRORS
}