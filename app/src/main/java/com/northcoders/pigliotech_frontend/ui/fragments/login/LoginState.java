package com.northcoders.pigliotech_frontend.ui.fragments.login;

public class LoginState {
    private final Boolean isLoading;

    public LoginState(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public Boolean getLoading() {
        return isLoading;
    }
}
