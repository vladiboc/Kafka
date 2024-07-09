package org.example.orderservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.libkafka.aop.Loggable;
import org.example.libkafka.model.OrderStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Слушает из кафки OrderStatusTopic
 * Если находит там объект OrderStatus, то логирует его
 */
@Component
@Slf4j
public class OrderStatusListener {
  @Loggable
  @KafkaListener(topics = {"${app.kafka.orderStatusTopic}"},
      groupId = "${app.kafka.orderStatusGroupId}",
      containerFactory = "orderStatusConcurrentKafkaListenerContainerFactory")
  public void listen(
      @Payload OrderStatus orderStatus,
      @Header(value = KafkaHeaders.RECEIVED_KEY, required = false)UUID key,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
    log.info("Получено сообщение о статусе заказа: {}", orderStatus);
    log.info("Параметры полученного сообщения: " +
        "Key: {}, Topic: {}, Partition: {}, Timestamp: {}", key, topic, partition, timestamp);
  }
}
