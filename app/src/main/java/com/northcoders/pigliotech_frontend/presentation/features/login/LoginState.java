package com.northcoders.pigliotech_frontend.presentation.features.login;

public class LoginState {
    private final Boolean isLoading;

    public LoginState(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public Boolean getLoading() {
        return isLoading;
    }
}
