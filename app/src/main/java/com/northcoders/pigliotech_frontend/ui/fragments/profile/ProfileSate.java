package com.northcoders.pigliotech_frontend.ui.fragments.profile;

public class ProfileSate {

    private final Boolean isLoading;

    public ProfileSate(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public Boolean getLoading() {
        return isLoading;
    }
}
