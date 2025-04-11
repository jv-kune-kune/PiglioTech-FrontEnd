package com.northcoders.pigliotech_frontend.presentation.features.home;

public interface HomeEvents {

    record ClickedUserLibrary(String clickedUserId) implements  HomeEvents {}
}
