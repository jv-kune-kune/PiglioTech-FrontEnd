package com.northcoders.pigliotech_frontend.data.models;

@SuppressWarnings("unused")
public record Match(Long id, User userOne, User userTwo, Book userOneBook, Book userTwoBook) {
}
