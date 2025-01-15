package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;



import java.util.List;


// The states that the ProfileFragment can be in
public interface SwapState {

    record Loading() implements SwapState {}

    record Loaded(
            List<Object> swaps
            // TODO update list type with Swap object
    ) implements SwapState {}

    // TODO LOOK INTO HANDLING ERRORS
}