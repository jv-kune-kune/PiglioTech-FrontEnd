package com.northcoders.pigliotech_frontend.ui.fragments.home;

import com.northcoders.pigliotech_frontend.model.User;

import java.util.List;

public interface HomeState {

    record Loading() implements HomeState {
    }

    record Loaded(List<User> otherUserLibraries) implements HomeState {
    }

    record Error() implements HomeState {
    }
}