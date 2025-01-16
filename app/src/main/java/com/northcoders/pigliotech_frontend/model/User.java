package com.northcoders.pigliotech_frontend.model;

import androidx.databinding.BaseObservable;

import java.util.List;

//TODO : possibly might need to extend parcelable
public class User extends BaseObservable {
    private String uid;
    private String name;
    private String email;
    private String region;
    private String thumbnail;
    private List<Book> books;

    // No args constructor
    public User() {}

    // All args
    public User(String uid, String name, String email, String region, String thumbnail, List<Book> books) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.region = region;
        this.thumbnail = thumbnail;
        this.books = books;
    }

    // New User constructor
    public User(String uid, String name, String email, String region, String thumbnail) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.region = region;
        this.thumbnail = thumbnail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Book> getBooks() {
        return books;
    }

    public String createLabel(List<Book> books) {
        if(books.size() == 1) {
            return "Book";
        }
        return "Books";
    }

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
