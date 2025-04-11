package com.northcoders.pigliotech_frontend.data.models;

import androidx.annotation.NonNull;

public class Book {

    private String isbn;
    private String title;
    private String author;
    private String thumbnail;

    // No args constructor
    public Book() {
    }

    // All args constructor
    public Book(String isbn, String title, String author, String thumbnail) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.thumbnail = thumbnail;
    }

    public String getIsbn() {
        return isbn;
    }

    @SuppressWarnings("unused")
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    @SuppressWarnings("unused")
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @SuppressWarnings("unused")
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @NonNull
    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
