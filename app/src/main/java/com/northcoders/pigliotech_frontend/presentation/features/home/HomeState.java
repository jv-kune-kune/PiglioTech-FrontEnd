package com.northcoders.pigliotech_frontend.presentation.features.home;

import com.northcoders.pigliotech_frontend.data.models.User;

import java.util.List;

public interface HomeState {

    record Loading() implements HomeState {}

    record Loaded(List<User> otherUserLibraries) implements HomeState {
    }

    record Error() implements HomeState {}
}