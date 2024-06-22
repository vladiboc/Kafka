package org.example.libkafka.model;

public record OrderEvent (
    String order,
    Integer quantity
) {}
