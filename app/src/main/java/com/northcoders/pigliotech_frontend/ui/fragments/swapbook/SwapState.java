package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;


import com.northcoders.pigliotech_frontend.model.Match;

import java.util.List;


// The states that the ProfileFragment can be in
public interface SwapState {

    record Loading() implements SwapState {
    }

    record Loaded(
            List<Match> matches
    ) implements SwapState {
    }

    record Error() implements SwapState {
    }

}