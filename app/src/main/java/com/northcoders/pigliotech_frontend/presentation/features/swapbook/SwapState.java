package com.northcoders.pigliotech_frontend.presentation.features.swapbook;



import com.northcoders.pigliotech_frontend.data.models.Match;

import java.util.List;


// The states that the ProfileFragment can be in
public interface SwapState {

    record Loading() implements SwapState {}

    record Loaded(
            List<Match> matches
    ) implements SwapState {}

    record Error () implements SwapState {}

}