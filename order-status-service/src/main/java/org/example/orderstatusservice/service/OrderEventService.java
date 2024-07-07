package org.example.orderstatusservice.service;

import org.example.libkafka.aop.Loggable;
import org.example.libkafka.model.OrderEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderEventService {
  private final List<OrderEvent> orderEventList = new ArrayList<>();

  @Loggable
  public void addEvent(OrderEvent event) {
    this.orderEventList.add(event);
  }

  @Loggable
  public Optional<OrderEvent> getEventByOrder(String order) {
    return this.orderEventList
        .stream()
        .filter(e -> e.order().equals(order))
        .findFirst();
  }
}
