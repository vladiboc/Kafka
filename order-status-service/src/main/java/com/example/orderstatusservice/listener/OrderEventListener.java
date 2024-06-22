package com.example.orderstatusservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.libkafka.aop.Loggable;
import org.example.libkafka.model.OrderEvent;
import org.example.libkafka.model.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {
  @Value("${app.kafka.orderStatusTopic}")
  private String orderStatusTopic;
  private final KafkaTemplate<String, OrderStatus> kafkaTemplate;

  @Loggable
  @KafkaListener(topics = {"${app.kafka.orderTopic}"},
      groupId = "${app.kafka.orderEventGroupId}",
      containerFactory = "orderEventConcurrentKafkaListenerContainerFactory")
  public void listen(
      @Payload OrderEvent orderEvent,
      @Header(value = KafkaHeaders.RECEIVED_KEY, required = false)UUID key,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
    log.info("Получено сообщение о заказе: {}", orderEvent);
    log.info("Параметры полученного сообщения: " +
        "Key: {}, Topic: {}, Partition: {}, Timestamp: {}", key, topic, partition, timestamp);
    this.kafkaTemplate.send(
        this.orderStatusTopic, new OrderStatus("Заказ создан!", Instant.now().getEpochSecond())
    );
  }
}