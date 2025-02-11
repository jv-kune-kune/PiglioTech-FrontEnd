package com.northcoders.pigliotech_frontend.ui.fragments.home;

public interface HomeEvents {

    record ClickedUserLibrary(String clickedUserId) implements HomeEvents {
    }
}
