package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import com.northcoders.pigliotech_frontend.model.Book;

import java.util.List;

public interface ProfileState {

    class Loading implements ProfileState {
        public Loading() {
        }

    }

    class Loaded implements ProfileState {
        private final String name;
        private final String email;
        private final int region;
        private final String artworkUrl;
        private final List<Book> books;

        public Loaded(String name, String email, int region, String artworkUrl, List<Book> books) {
            this.name = name;
            this.email = email;
            this.region = region;
            this.artworkUrl = artworkUrl;
            this.books = books;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public int getRegion() {
            return region;
        }

        public String getArtworkUrl() {
            return artworkUrl;
        }

        public List<Book> getBooks() {
            return books;
        }
    }

    // TODO LOOK INTO HANDLING ERRORS
}
