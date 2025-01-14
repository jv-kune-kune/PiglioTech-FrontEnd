package com.northcoders.pigliotech_frontend.ui.fragments.home;

public class HomeEvents {

    private final String clickedUserId;

    HomeEvents(String s) {
        this.clickedUserId = s;
    }

    public String getClickedUserId() {
        return clickedUserId;
    }
}
