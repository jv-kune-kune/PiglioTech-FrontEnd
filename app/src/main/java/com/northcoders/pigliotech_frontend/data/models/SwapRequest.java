package com.northcoders.pigliotech_frontend.data.models;

public record SwapRequest(String initiatorUid,
                          String receiverUid,
                          String receiverIsbn) {
}
