package com.northcoders.pigliotech_frontend.ui.fragments.home;

import com.northcoders.pigliotech_frontend.model.User;

import java.util.List;

public interface HomeState {

    class Loading implements HomeState {
        public Loading() {
        }
    }

    class Loaded implements HomeState {
        private final List<User> otherUserLibraries;

        public Loaded(List<User> otherUserLibraries) {
            this.otherUserLibraries = otherUserLibraries;
        }

        public List<User> getOtherUserLibraries() {
            return otherUserLibraries;
        }
    }
}