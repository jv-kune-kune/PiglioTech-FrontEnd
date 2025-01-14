package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import com.northcoders.pigliotech_frontend.model.Book;

import java.util.List;

public interface ProfileState {

    record Loading(boolean isUser) implements ProfileState {

    }

    record Loaded(String name,
                  String email,
                  int region,
                  String artworkUrl,
                  List<Book> books) implements ProfileState {
    }

    record OtherUserLoaded(String name,
                  String email,
                  int region,
                  String artworkUrl,
                  List<Book> books) implements ProfileState {
    }



    // TODO LOOK INTO HANDLING ERRORS
}
