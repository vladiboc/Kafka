package org.example.orderservice.web.v1.dto;

public record OrderUpsertRequest(
    String order,
    Integer quantity
) {}
