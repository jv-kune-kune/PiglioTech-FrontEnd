package com.northcoders.pigliotech_frontend.presentation.features.addbook;

public class AddBookState {
    private final Boolean isLoading;

    public AddBookState(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public Boolean getLoading() {
        return isLoading;
    }
}
