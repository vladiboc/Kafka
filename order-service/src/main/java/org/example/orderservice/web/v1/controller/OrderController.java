package org.example.orderservice.web.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.libkafka.aop.Loggable;
import org.example.libkafka.model.OrderEvent;
import org.example.orderservice.mapper.v1.OrderMapper;
import org.example.orderservice.web.v1.dto.ErrorMsgResponse;
import org.example.orderservice.web.v1.dto.OrderUpsertRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Заказы через Kafka", description = "Отправка заказов в Kafka и получение ответа.")
public class OrderController {
  @Value("${app.kafka.orderTopic}")
  private String orderTopic;
  private final OrderMapper orderMapper;
  public final KafkaTemplate<String, OrderEvent> kafkaTemplate;

  @Operation(
      summary = "Отправить заказ в Kafka.",
      description = "Возвращает полезную нагрузку сообщения, отправленного в Kafka.")
  @ApiResponse(
      responseCode = "201",
      content = {@Content(
          schema = @Schema(implementation = OrderEvent.class),
          mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      description = "При некорректных параметрах заказа.",
      content = {@Content(
          schema = @Schema(implementation = ErrorMsgResponse.class),
          mediaType = "application/json")})
  @Loggable
  @PostMapping
  public ResponseEntity<OrderEvent> create(@RequestBody @Valid OrderUpsertRequest request) {
    final var orderEvent = this.orderMapper.requestToOrderEvent(request);

    this.kafkaTemplate.send(this.orderTopic, orderEvent);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderEvent);
  }
}
