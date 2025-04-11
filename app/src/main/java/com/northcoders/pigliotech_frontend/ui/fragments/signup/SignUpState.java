package com.northcoders.pigliotech_frontend.ui.fragments.signup;

public class SignUpState{
    private final Boolean isLoading;

    public SignUpState(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public Boolean getLoading() {
        return isLoading;
    }
}
