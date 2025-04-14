package com.northcoders.pigliotech_frontend.data.models;

import androidx.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;

public class User extends BaseObservable {
    private String uid;
    private String name;
    private String email;
    private String region;
    private String thumbnail;
    private List<Book> books;

    // No args constructor
    public User() {
        this.books = new ArrayList<>();
    }

    // All args
    public User(String uid, String name, String email, String region, String thumbnail, List<Book> books) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.region = region;
        this.thumbnail = thumbnail;
        this.books = books != null ? books : new ArrayList<>();
    }

    // New User constructor
    public User(String uid, String name, String email, String region, String thumbnail) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.region = region;
        this.thumbnail = thumbnail;
        this.books = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    @SuppressWarnings("unused")
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    @SuppressWarnings("unused")
    public void setRegion(String region) {
        this.region = region;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @SuppressWarnings("unused")
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Book> getBooks() {
        return books;
    }

    @SuppressWarnings("unused")
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @SuppressWarnings("unused")
    public String createLabel(List<Book> books) {
        if (books == null) {
            return "Books";
        }
        if (books.size() == 1) {
            return "Book";
        }
        return "Books";
    }

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", region='" + region + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }

}
