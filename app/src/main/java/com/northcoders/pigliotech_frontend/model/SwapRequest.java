package com.northcoders.pigliotech_frontend.model;

public record SwapRequest(String initiatorUid,
                          String receiverUid,
                          String receiverIsbn) {
}
