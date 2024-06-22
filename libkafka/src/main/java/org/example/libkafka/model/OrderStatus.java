package org.example.libkafka.model;

public record OrderStatus(
    String status,
    Long timestamp
) {
}
