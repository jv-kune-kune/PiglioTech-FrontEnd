package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import com.northcoders.pigliotech_frontend.model.Book;

import java.util.List;

// The states that the ProfileFragment can be in
public interface ProfileState {

    record Loading() implements ProfileState {
    }

    record Loaded(
            String name,
            String email,
            int region,
            String artworkUrl,
            List<Book> books
    ) implements ProfileState {
    }

    record OtherUserLoaded(
            String name,
            String email,
            int region,
            String artworkUrl,
            List<Book> books
    ) implements ProfileState {
    }

    record Error() implements ProfileState {
    }
}