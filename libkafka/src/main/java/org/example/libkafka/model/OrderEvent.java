package org.example.libkafka.model;

/**
 * Сущность "Событие заказа"
 * одикаковая как для сервиса OrderService, так и для сервиса OrderStatusService
 * поэтому вынесена в общую библиотеку - подпроект LibKafka
 */
public record OrderEvent (
    String order,
    Integer quantity
) {}
