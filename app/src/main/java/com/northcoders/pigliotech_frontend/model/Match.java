package com.northcoders.pigliotech_frontend.model;

public record Match(Long id, User userOne, User userTwo, Book userOneBook, Book userTwoBook) {
}
